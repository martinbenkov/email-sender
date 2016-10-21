package com.example.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.example.configuration.ConfigurationManager;
import com.example.domain.Email;
import com.example.domain.SendStatus;

@Component
public class EmailDaoImpl extends NamedParameterJdbcDaoSupport  implements EmailDao 
{
	private final static Logger logger = LoggerFactory.getLogger(EmailDaoImpl.class);
	
	private static final int BACKOFF_PERIOD = 60 * 1000;
	
	@Autowired
	private ConfigurationManager configurationManager;
	
	@Autowired 
	public EmailDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}
	
	@Override
	public void save(Email email)
	{
		String sql = "insert into `email` (`from`, `to`, `subject`, `emailtext`, `status`, `uid`, `last_updated`, `attempts`)"
				+ "values(:from, :to, :subject, :text, :status, :uid, :now, :attempts)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("from", email.getFrom());
		p.put("to", email.getTo());
		p.put("subject", email.getSubject());
		p.put("text", email.getText());
		p.put("status", "pending");
		p.put("uid", UUID.randomUUID().toString());
		p.put("now", System.currentTimeMillis());
		p.put("attempts", 0);

		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(p), keyHolder);

		Long emailId = keyHolder.getKey().longValue();
		email.setId(emailId);
	}

	@Override
	public void markAsSent(final Email email)
	{
		if (email.isPing())
		{
			return;
		}
		updateStatus(email.getId(), SendStatus.Sent);
	}

	@Override
	public Email getPending()
	{
		String sql = "select id, uid from email where status='pending' and last_updated < :time"
				+ "order by last_updated limit 10";
		long time = System.currentTimeMillis() - BACKOFF_PERIOD;
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("time", time);
		
		final Map<Long, String> pending = new HashMap<Long, String>();
		
		getNamedParameterJdbcTemplate().query(sql, new MapSqlParameterSource(p), 
				new RowMapper<SendStatus>()
		{

			@Override
			public SendStatus mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Long id = rs.getLong(1);
				String uid = rs.getString(2);
				pending.put(id, uid);
				return null;
			}
		});
		for (Long id : pending.keySet())
		{
			String uid = pending.get(id);
			if (tryToGetLock(id, uid))
			{
				return getEmail(id);
			}
		}
		
		return null;
	}

	private Email getEmail(Long id)
	{
		String sql = "select * from email where id=:id";
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", id);
		
		final Email email = new Email();
		
		getNamedParameterJdbcTemplate().query(sql, new MapSqlParameterSource(p), 
				new RowMapper<SendStatus>()
		{

			@Override
			public SendStatus mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				email.setId(rs.getLong("id"));
				email.setFrom(rs.getString("from"));
				email.setText(rs.getString("emailtext"));
				email.setTo(rs.getString("to"));
				email.setSubject(rs.getString("subject"));
				return null;
			}
		});
		return email;
	}

	/**
	 * With multiple instances of the service running they may get the same email and try to send it
	 * This is why we update the uid and only one instance will be able to do it.
	 */
	private boolean tryToGetLock(Long id, String uid)
	{
		String sql = "update email set uid=:newUid, last_updated=:now where id=:id and uid=:uid";
		
		String newUid = UUID.randomUUID().toString();
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", id);
		p.put("uid", uid);
		p.put("newUid", newUid);
		p.put("now", System.currentTimeMillis());
		
		int rows = getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(p));
		
		return rows > 0;
	}

	@Override
	public void markAsRejected(Email email)
	{
		if (email.isPing())
		{
			return;
		}
		updateStatus(email.getId(), SendStatus.Rejected);
	}

	@Override
	public void markAsPending(Email email)
	{
		if (email.isPing())
		{
			return;
		}
		int currentAttempts = getAttemtps(email.getId());
		if (currentAttempts == -1)
		{
			logger.debug("womething wrong with email " + email.getId());
			return;
		}

		if (currentAttempts < configurationManager.getRetrySentAttempts())
		{
			updateAttemtps(email.getId(), currentAttempts++);
		}
		else
		{
			updateStatus(email.getId(), SendStatus.NotSent);
		}
	}

	private void updateStatus(long id, SendStatus status)
	{
		String sql = "update email set status=:status, last_updated=:now where id = :id";
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", id);
		p.put("status", status.toString());
		p.put("now", System.currentTimeMillis());

		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(p));
		
	}

	private void updateAttemtps(long id, int attemtps)
	{
		String sql = "update email set attempts=:attempts, last_updated=:now where id = :id";
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", id);
		p.put("attempts", attemtps);
		p.put("now", System.currentTimeMillis());

		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(p));
		
	}

	@Override
	public SendStatus getStatus(long emailId)
	{
		String sql = "select status from email where id = :id";
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", emailId);
		
		final List<SendStatus> res = new ArrayList<SendStatus>();
		
		getNamedParameterJdbcTemplate().query(sql, new MapSqlParameterSource(p), 
				new RowMapper<SendStatus>()
		{

			@Override
			public SendStatus mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				String s = rs.getString(1);
				res.add(SendStatus.valueOf(s));
				return null;
			}
		});
		if (res.isEmpty())
		{
			logger.debug("no email with id " + emailId);
			return null;
		}
		else
		{
			return res.get(0);
		}
	}
	
	private int getAttemtps(long emailId)
	{
		String sql = "select attempts from email where id = :id";
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("id", emailId);
		
		final List<Integer> res = new ArrayList<Integer>();
		
		getNamedParameterJdbcTemplate().query(sql, new MapSqlParameterSource(p), 
				new RowMapper<SendStatus>()
		{

			@Override
			public SendStatus mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				int i = rs.getInt(1);
				res.add(i);
				return null;
			}
		});
		if (res.isEmpty())
		{
			return -1;
		}
		else
		{
			return res.get(0);
		}
	}
}
