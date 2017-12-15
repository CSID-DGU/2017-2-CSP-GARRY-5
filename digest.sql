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
-- 테이블 구조 `digest`
--

CREATE TABLE IF NOT EXISTS `digest` (
`id` int(11) NOT NULL,
  `phonenumber` varchar(40) NOT NULL,
  `date` varchar(40) NOT NULL,
  `peecount` varchar(40) NOT NULL DEFAULT '0',
  `shitcount` varchar(40) NOT NULL DEFAULT '0',
  `totalcount` varchar(40) NOT NULL DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- 테이블의 덤프 데이터 `digest`
--

INSERT INTO `digest` (`id`, `phonenumber`, `date`, `peecount`, `shitcount`, `totalcount`) VALUES
(13, '01055555555', '20171205', '2', '3', '5');

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `digest`
--
ALTER TABLE `digest`
 ADD PRIMARY KEY (`id`), ADD KEY `phonenumber` (`phonenumber`);

--
-- 덤프된 테이블의 AUTO_INCREMENT
--

--
-- 테이블의 AUTO_INCREMENT `digest`
--
ALTER TABLE `digest`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
--
-- 덤프된 테이블의 제약사항
--

--
-- 테이블의 제약사항 `digest`
--
ALTER TABLE `digest`
ADD CONSTRAINT `fk_phonenumber` FOREIGN KEY (`phonenumber`) REFERENCES `profile` (`phonenumber`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
