-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 17-12-15 06:52
-- 서버 버전: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db`
--

-- --------------------------------------------------------

--
-- 테이블 구조 `profile`
--

CREATE TABLE IF NOT EXISTS `profile` (
  `phonenumber` varchar(40) NOT NULL,
  `nickname` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `age` int(11) NOT NULL,
  `sex` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 테이블의 덤프 데이터 `profile`
--

INSERT INTO `profile` (`phonenumber`, `nickname`, `name`, `age`, `sex`) VALUES
('01022036458', 'queen', 'kang', 1, 'female'),
('01055555555', 'acrobat', 'kimNY', 2, 'female'),
('01068778052', 'ksmark', 'Lee', 2, 'male'),
('01080301501', 'ddong2', 'LeeMyeongSeop', 4, 'male'),
('01080301504', 'monkey', 'KimYY', 3, 'Male'),
('01091328052', 'mama', 'kim', 3, 'Male'),
('01099997776', 'ayobro', 'yoonkiyoung', 2, 'female');

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `profile`
--
ALTER TABLE `profile`
 ADD PRIMARY KEY (`phonenumber`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
