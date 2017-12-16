CREATE TABLE `test_data` (
  `ue_id` char(16) DEFAULT NULL,
  `start_time` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `bytes_upload` int(11) DEFAULT NULL,
  `bytes_download` int(11) DEFAULT NULL,
  `cell_id` int(11) DEFAULT NULL,
  `mcc` int(11) DEFAULT NULL,
  `mnc` int(11) DEFAULT NULL,
  `lac` int(11) DEFAULT NULL,
  `imei` char(16) DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;