create database email_sender_db
;

use email_sender_db
;

CREATE TABLE `email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from` varchar(100) NOT NULL,
  `to` varchar(100) NOT NULL,
  `subject` varchar(1000) NOT NULL,
  `emailtext` varchar(10000) DEFAULT NULL,
  `status` enum('Sent','Rejected','Pending','Notsent') NOT NULL,
  `uid` varchar(36) DEFAULT NULL,
  `last_updated` bigint(64) DEFAULT NULL,
  `attempts` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;