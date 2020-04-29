-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 29, 2020 at 08:26 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `errand`
--

-- --------------------------------------------------------

--
-- Table structure for table `api_achievement`
--

CREATE TABLE `api_achievement` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(256) NOT NULL,
  `icon` varchar(100) NOT NULL,
  `requirements` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_achievement`
--

INSERT INTO `api_achievement` (`id`, `name`, `description`, `icon`, `requirements`) VALUES
(1, 'Pouzdan korisnik', 'Veliki broj pozitivnih usluga', '', 'Some SQL query?'),
(2, 'Brz korisnik', 'Veliki broj brzo ispunjenih usluga', '', 'Some SQL query?'),
(3, 'Eko korisnik', 'Usluge izvrsava bez koriscenja motornih vozila', '', 'Pseudo SQL query?');

-- --------------------------------------------------------

--
-- Table structure for table `api_achievementlevel`
--

CREATE TABLE `api_achievementlevel` (
  `id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_address`
--

CREATE TABLE `api_address` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `home` tinyint(1) NOT NULL,
  `arrived` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_address`
--

INSERT INTO `api_address` (`id`, `name`, `longitude`, `latitude`, `home`, `arrived`) VALUES
(3, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 0, 0),
(4, 'Majora Tepica 5/6', 0, 0, 1, 0),
(5, 'Vojvode Tankosica 7/3', 0, 0, 0, 0),
(6, 'Vojvode Tankosica 10', 0, 0, 0, 0),
(7, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 1, 0),
(8, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `api_banned`
--

CREATE TABLE `api_banned` (
  `id` int(11) NOT NULL,
  `until` datetime(6) NOT NULL,
  `comment` varchar(256) NOT NULL,
  `banned_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_benefit`
--

CREATE TABLE `api_benefit` (
  `id` int(11) NOT NULL,
  `discount` double NOT NULL,
  `benefit_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_benefit`
--

INSERT INTO `api_benefit` (`id`, `discount`, `benefit_user_id`) VALUES
(1, 0.1, 6),
(2, 0.2, 2),
(3, 0.1, 2),
(4, 0.1, 4),
(5, 0.1, 2),
(6, 0.1, 2),
(7, 0.1, 3),
(8, 0.1, 3),
(9, 0.1, 3),
(10, 0.1, 3),
(11, 0.1, 3),
(12, 0.1, 3),
(13, 0.1, 3),
(14, 0.1, 3),
(15, 0.1, 3),
(16, 0.1, 3),
(17, 0.1, 3),
(18, 0.1, 3),
(19, 0.1, 3),
(20, 0.1, 3),
(21, 0.1, 3),
(22, 0.1, 3),
(23, 0.1, 3),
(24, 0.1, 3),
(25, 0.1, 3),
(26, 0.1, 3),
(27, 0.1, 3),
(28, 0.1, 3),
(29, 0.1, 3),
(30, 0.1, 3),
(31, 0.1, 6);

-- --------------------------------------------------------

--
-- Table structure for table `api_checklist`
--

CREATE TABLE `api_checklist` (
  `id` int(11) NOT NULL,
  `check_list` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_checklist`
--

INSERT INTO `api_checklist` (`id`, `check_list`) VALUES
(3, '10 jaja'),
(4, 'mleko 1l'),
(5, 'jogurt 2l'),
(6, 'hleb'),
(7, 'crna cokolada (njlepse zelje)'),
(8, '6 banane'),
(9, 'mesano mleveno meso 500g');

-- --------------------------------------------------------

--
-- Table structure for table `api_fullrequest`
--

CREATE TABLE `api_fullrequest` (
  `id` int(11) NOT NULL,
  `accepted_offer_id` int(11) DEFAULT NULL,
  `rating_id` int(11) DEFAULT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fullrequest`
--

INSERT INTO `api_fullrequest` (`id`, `accepted_offer_id`, `rating_id`, `request_id`) VALUES
(1, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `api_fullrequest_offers`
--

CREATE TABLE `api_fullrequest_offers` (
  `id` int(11) NOT NULL,
  `fullrequest_id` int(11) NOT NULL,
  `offer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser`
--

CREATE TABLE `api_fulluser` (
  `id` int(11) NOT NULL,
  `benefit_discount` double DEFAULT NULL,
  `benefit_requirement` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser`
--

INSERT INTO `api_fulluser` (`id`, `benefit_discount`, `benefit_requirement`, `user_id`) VALUES
(1, NULL, NULL, 1),
(2, NULL, NULL, 2),
(3, NULL, NULL, 3),
(4, NULL, NULL, 4),
(5, NULL, NULL, 5),
(6, NULL, NULL, 6),
(7, NULL, NULL, 7),
(8, NULL, NULL, 8),
(9, NULL, NULL, 9);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_achievements`
--

CREATE TABLE `api_fulluser_achievements` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `achievementlevel_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_addresses`
--

CREATE TABLE `api_fulluser_addresses` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_addresses`
--

INSERT INTO `api_fulluser_addresses` (`id`, `fulluser_id`, `address_id`) VALUES
(1, 2, 4),
(2, 3, 3),
(3, 3, 5),
(5, 3, 7),
(4, 4, 6),
(6, 6, 8);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_benefitlist`
--

CREATE TABLE `api_fulluser_benefitlist` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `benefit_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_benefitlist`
--

INSERT INTO `api_fulluser_benefitlist` (`id`, `fulluser_id`, `benefit_id`) VALUES
(1, 4, 1),
(3, 4, 30),
(4, 4, 31),
(2, 6, 2);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_blocked`
--

CREATE TABLE `api_fulluser_blocked` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_blocked`
--

INSERT INTO `api_fulluser_blocked` (`id`, `fulluser_id`, `user_id`) VALUES
(2, 3, 4),
(1, 4, 5);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_notifications`
--

CREATE TABLE `api_fulluser_notifications` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_offers`
--

CREATE TABLE `api_fulluser_offers` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `offer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_ratings`
--

CREATE TABLE `api_fulluser_ratings` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `rating_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_requests`
--

CREATE TABLE `api_fulluser_requests` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_requests`
--

INSERT INTO `api_fulluser_requests` (`id`, `fulluser_id`, `request_id`) VALUES
(1, 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_services`
--

CREATE TABLE `api_fulluser_services` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `userservice_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_services`
--

INSERT INTO `api_fulluser_services` (`id`, `fulluser_id`, `userservice_id`) VALUES
(5, 2, 5),
(1, 3, 1),
(2, 3, 2),
(3, 5, 3),
(4, 5, 4);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_working_hours`
--

CREATE TABLE `api_fulluser_working_hours` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `workinghour_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_fulluser_working_hours`
--

INSERT INTO `api_fulluser_working_hours` (`id`, `fulluser_id`, `workinghour_id`) VALUES
(1, 3, 1),
(2, 5, 2),
(3, 5, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_notification`
--

CREATE TABLE `api_notification` (
  `id` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `body` varchar(256) NOT NULL,
  `notification_type` int(11) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_offer`
--

CREATE TABLE `api_offer` (
  `id` int(11) NOT NULL,
  `payment_type` varchar(4) NOT NULL,
  `payment_ammount` double NOT NULL,
  `created_by_id` int(11) DEFAULT NULL,
  `edit_id` int(11) DEFAULT NULL,
  `request_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_picture`
--

CREATE TABLE `api_picture` (
  `id` int(11) NOT NULL,
  `picture` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_rating`
--

CREATE TABLE `api_rating` (
  `id` int(11) NOT NULL,
  `grade` double NOT NULL,
  `comment` varchar(256) NOT NULL,
  `created_by_id` int(11) DEFAULT NULL,
  `rated_user_id` int(11) DEFAULT NULL,
  `request_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_report`
--

CREATE TABLE `api_report` (
  `id` int(11) NOT NULL,
  `comment` varchar(256) NOT NULL,
  `report_type` int(11) NOT NULL,
  `created_by_id` int(11) NOT NULL,
  `reported_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_request`
--

CREATE TABLE `api_request` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `status` int(11) NOT NULL,
  `location_status` int(11) NOT NULL,
  `time` datetime(6) NOT NULL,
  `picture_required` tinyint(1) NOT NULL,
  `note` varchar(256) NOT NULL,
  `request_type` int(11) NOT NULL,
  `max_dist` double NOT NULL,
  `min_rating` double NOT NULL,
  `created_by_id` int(11) DEFAULT NULL,
  `service_type_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_request`
--

INSERT INTO `api_request` (`id`, `name`, `status`, `location_status`, `time`, `picture_required`, `note`, `request_type`, `max_dist`, `min_rating`, `created_by_id`, `service_type_id`) VALUES
(1, 'kupovina', 0, 0, '2020-03-19 14:35:47.000000', 0, 'none', 0, 10, 3.5, 4, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_requestedit`
--

CREATE TABLE `api_requestedit` (
  `id` int(11) NOT NULL,
  `time` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_requestedit_request`
--

CREATE TABLE `api_requestedit_request` (
  `id` int(11) NOT NULL,
  `requestedit_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_requestedit_tasks`
--

CREATE TABLE `api_requestedit_tasks` (
  `id` int(11) NOT NULL,
  `requestedit_id` int(11) NOT NULL,
  `taskedit_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_request_pictures`
--

CREATE TABLE `api_request_pictures` (
  `id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `picture_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_request_tasklist`
--

CREATE TABLE `api_request_tasklist` (
  `id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_request_tasklist`
--

INSERT INTO `api_request_tasklist` (`id`, `request_id`, `task_id`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `api_service`
--

CREATE TABLE `api_service` (
  `id` int(11) NOT NULL,
  `service_type` varchar(50) NOT NULL,
  `description` varchar(256) NOT NULL,
  `picture_required` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_service`
--

INSERT INTO `api_service` (`id`, `service_type`, `description`, `picture_required`) VALUES
(3, 'Kupovina', 'Napisati spisak proizvoda za kupovinu', 0),
(4, 'Placanje racuna', 'Uneti potrebne racune za placanje', 0);

-- --------------------------------------------------------

--
-- Table structure for table `api_task`
--

CREATE TABLE `api_task` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(256) NOT NULL,
  `picture_required` tinyint(1) NOT NULL,
  `address_id` int(11) DEFAULT NULL,
  `service_type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_task`
--

INSERT INTO `api_task` (`id`, `name`, `description`, `picture_required`, `address_id`, `service_type_id`) VALUES
(1, 'kupovina', 'kupiti u maxi prodavnici', 0, NULL, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_taskedit`
--

CREATE TABLE `api_taskedit` (
  `id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_task_checklist`
--

CREATE TABLE `api_task_checklist` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `checklist_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_task_checklist`
--

INSERT INTO `api_task_checklist` (`id`, `task_id`, `checklist_id`) VALUES
(2, 1, 3),
(5, 1, 5),
(4, 1, 6),
(3, 1, 7),
(1, 1, 8);

-- --------------------------------------------------------

--
-- Table structure for table `api_task_pictures`
--

CREATE TABLE `api_task_pictures` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `picture_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_user`
--

CREATE TABLE `api_user` (
  `id` int(11) NOT NULL,
  `password` varchar(128) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `username` varchar(150) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(150) NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `date_joined` datetime(6) NOT NULL,
  `email` varchar(254) NOT NULL,
  `phone` varchar(16) NOT NULL,
  `picture` varchar(100) DEFAULT NULL,
  `avg_rating` double DEFAULT NULL,
  `min_rating` double DEFAULT NULL,
  `max_dist` double DEFAULT NULL,
  `status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_user`
--

INSERT INTO `api_user` (`id`, `password`, `last_login`, `is_superuser`, `username`, `first_name`, `last_name`, `is_staff`, `is_active`, `date_joined`, `email`, `phone`, `picture`, `avg_rating`, `min_rating`, `max_dist`, `status`) VALUES
(1, 'pbkdf2_sha256$180000$f8843rwWRhHu$6kxGD/03+Iqc+gnmvlZF/SKbSl/geDui2O2R4R2MKqw=', '2020-04-26 18:24:34.327935', 0, 'admin@errand.com', 'Admin', 'Admin', 0, 1, '2020-03-18 21:58:58.931534', 'admin@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(2, 'pbkdf2_sha256$180000$qAPSFSrxX0B7$Q2dJ7h01aCLTys0L/0B7T8bbgHJ3PsYZg83AD0fsgQE=', NULL, 0, 'nina@errand.com', 'Nina', 'Lazovic', 0, 1, '2020-03-18 22:01:38.551541', 'nina@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(3, 'pbkdf2_sha256$180000$5b2asD3fVVQM$7Wboikyuh6H2+HSLm49AGs1XWiAHKdgdfS/DmdH13tk=', NULL, 0, 'djordjepav@errand.com', 'Djordje', 'Pavlovic', 0, 1, '2020-03-18 22:03:56.922918', 'djordjepav@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(4, 'pbkdf2_sha256$180000$Bry5i0Xj4Agy$U9eDS274ZzbQHFsCkeyjFXkiMiUK7rJABtsMSQrlkEY=', NULL, 0, 'milivojevicu@errand.com', 'Uros', 'Milivojevic', 0, 1, '2020-03-18 22:04:50.086419', 'milivojevicu@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(5, 'pbkdf2_sha256$180000$cCQ1pKwAIQBI$D6fie8wCaOU3cfJvkhcOmbwSbTPQYxRG+1rOjrno6HE=', NULL, 0, 'masa@errand.com', 'Masa', 'Nesic', 0, 1, '2020-03-18 22:06:03.779677', 'masa@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(6, 'pbkdf2_sha256$180000$fBYbA5QtM4WP$BCoFEUNg83esErQkE2OuzZvxFs9RhVBvl/cBDmG1Zy0=', NULL, 0, 'petar@errand.com', 'Petar', 'Trifunovic', 0, 1, '2020-03-18 22:06:44.987015', 'petar@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(7, 'pbkdf2_sha256$180000$wrfgHaubt7Jm$lGpY1fcD4ZzlfTy+e4UFEIYMRFv0CDLDWAImvDShl6c=', NULL, 0, 'u1@errand.com', 'u1', 'u1', 0, 1, '2020-04-26 15:03:30.114961', 'u1@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(8, 'pbkdf2_sha256$180000$5fMQ2Ccf9mPh$UE1jxSptD3NWQMMgzjaOmQRClcVOJaJPOW2DelDSPd0=', NULL, 0, 'u2@errand.com', 'u2', 'u2', 0, 1, '2020-04-26 15:48:39.283617', 'u2@errand.com', '+381123456789', '', NULL, NULL, NULL, 0),
(9, 'pbkdf2_sha256$180000$Rkd9lv80SXr3$I62AXzfuQ1zj5V9+Qap/fzJonJzoGbJ8tO8yCb5wXXA=', NULL, 0, 'u3@errand.com', 'u3', 'u3', 0, 1, '2020-04-26 18:27:10.920398', 'u3@errand.com', '+381123456789', '', NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `api_userservice`
--

CREATE TABLE `api_userservice` (
  `id` int(11) NOT NULL,
  `max_dist` double NOT NULL,
  `payment_type` varchar(4) NOT NULL,
  `payment_ammount` double NOT NULL,
  `min_rating` double NOT NULL,
  `service_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_userservice`
--

INSERT INTO `api_userservice` (`id`, `max_dist`, `payment_type`, `payment_ammount`, `min_rating`, `service_id`) VALUES
(1, 5, 'fix', 500, 3.5, 3),
(2, 5, 'fix', 300, 4.1, 4),
(3, 10, 'h', 1000, 3.8, 4),
(4, 10, 'h', 550, 4.5, 3),
(5, 10, 'h', 600, 4.2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_user_groups`
--

CREATE TABLE `api_user_groups` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_user_user_permissions`
--

CREATE TABLE `api_user_user_permissions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `api_workinghour`
--

CREATE TABLE `api_workinghour` (
  `id` int(11) NOT NULL,
  `day` varchar(3) NOT NULL,
  `work_from` time(6) NOT NULL,
  `work_until` time(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `api_workinghour`
--

INSERT INTO `api_workinghour` (`id`, `day`, `work_from`, `work_until`) VALUES
(1, 'tue', '09:00:00.000000', '15:00:00.000000'),
(2, 'mon', '10:00:00.000000', '17:00:00.000000'),
(3, 'mon', '18:00:00.000000', '19:45:00.000000');

-- --------------------------------------------------------

--
-- Table structure for table `authtoken_token`
--

CREATE TABLE `authtoken_token` (
  `key` varchar(40) NOT NULL,
  `created` datetime(6) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `authtoken_token`
--

INSERT INTO `authtoken_token` (`key`, `created`, `user_id`) VALUES
('1c0a605095b5edc42bcb5e50e54b5c53ffa24ffd', '2020-03-18 22:06:45.119743', 6),
('265b777d262ec5a7cb6dc9880e359dbcd83bce72', '2020-03-18 21:58:59.172861', 1),
('27209cf3b82d59d237becdacf13928b8bd88d065', '2020-03-18 22:06:03.888102', 5),
('293823531c407b8569b5b3aae88eeec9c4e602d2', '2020-04-26 15:03:30.477625', 7),
('5b7eec99bbc94b58103ebaa8a82e6a79e1c23b50', '2020-03-18 22:03:57.032307', 3),
('5eca2b5aa74583f00010069bcb541b67697d435e', '2020-04-26 18:27:11.035436', 9),
('706b517aca60ef1b5e51136d7ec9a8914086c82b', '2020-03-18 22:04:50.215397', 4),
('b3d90ed6891681ff8571333d5c3e9934e2855a99', '2020-03-18 22:01:38.686376', 2),
('d535600cd02068a40337c7a9c98a63e5e2ca348f', '2020-04-26 15:48:39.390253', 8);

-- --------------------------------------------------------

--
-- Table structure for table `auth_group`
--

CREATE TABLE `auth_group` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `auth_group_permissions`
--

CREATE TABLE `auth_group_permissions` (
  `id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `auth_permission`
--

CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `auth_permission`
--

INSERT INTO `auth_permission` (`id`, `name`, `content_type_id`, `codename`) VALUES
(1, 'Can add log entry', 1, 'add_logentry'),
(2, 'Can change log entry', 1, 'change_logentry'),
(3, 'Can delete log entry', 1, 'delete_logentry'),
(4, 'Can view log entry', 1, 'view_logentry'),
(5, 'Can add permission', 2, 'add_permission'),
(6, 'Can change permission', 2, 'change_permission'),
(7, 'Can delete permission', 2, 'delete_permission'),
(8, 'Can view permission', 2, 'view_permission'),
(9, 'Can add group', 3, 'add_group'),
(10, 'Can change group', 3, 'change_group'),
(11, 'Can delete group', 3, 'delete_group'),
(12, 'Can view group', 3, 'view_group'),
(13, 'Can add content type', 4, 'add_contenttype'),
(14, 'Can change content type', 4, 'change_contenttype'),
(15, 'Can delete content type', 4, 'delete_contenttype'),
(16, 'Can view content type', 4, 'view_contenttype'),
(17, 'Can add session', 5, 'add_session'),
(18, 'Can change session', 5, 'change_session'),
(19, 'Can delete session', 5, 'delete_session'),
(20, 'Can view session', 5, 'view_session'),
(21, 'Can add user', 6, 'add_user'),
(22, 'Can change user', 6, 'change_user'),
(23, 'Can delete user', 6, 'delete_user'),
(24, 'Can view user', 6, 'view_user'),
(25, 'Can add achievement', 7, 'add_achievement'),
(26, 'Can change achievement', 7, 'change_achievement'),
(27, 'Can delete achievement', 7, 'delete_achievement'),
(28, 'Can view achievement', 7, 'view_achievement'),
(29, 'Can add achievement level', 8, 'add_achievementlevel'),
(30, 'Can change achievement level', 8, 'change_achievementlevel'),
(31, 'Can delete achievement level', 8, 'delete_achievementlevel'),
(32, 'Can view achievement level', 8, 'view_achievementlevel'),
(33, 'Can add address', 9, 'add_address'),
(34, 'Can change address', 9, 'change_address'),
(35, 'Can delete address', 9, 'delete_address'),
(36, 'Can view address', 9, 'view_address'),
(37, 'Can add benefit', 10, 'add_benefit'),
(38, 'Can change benefit', 10, 'change_benefit'),
(39, 'Can delete benefit', 10, 'delete_benefit'),
(40, 'Can view benefit', 10, 'view_benefit'),
(41, 'Can add check list', 11, 'add_checklist'),
(42, 'Can change check list', 11, 'change_checklist'),
(43, 'Can delete check list', 11, 'delete_checklist'),
(44, 'Can view check list', 11, 'view_checklist'),
(45, 'Can add notification', 12, 'add_notification'),
(46, 'Can change notification', 12, 'change_notification'),
(47, 'Can delete notification', 12, 'delete_notification'),
(48, 'Can view notification', 12, 'view_notification'),
(49, 'Can add picture', 13, 'add_picture'),
(50, 'Can change picture', 13, 'change_picture'),
(51, 'Can delete picture', 13, 'delete_picture'),
(52, 'Can view picture', 13, 'view_picture'),
(53, 'Can add request', 14, 'add_request'),
(54, 'Can change request', 14, 'change_request'),
(55, 'Can delete request', 14, 'delete_request'),
(56, 'Can view request', 14, 'view_request'),
(57, 'Can add service', 15, 'add_service'),
(58, 'Can change service', 15, 'change_service'),
(59, 'Can delete service', 15, 'delete_service'),
(60, 'Can view service', 15, 'view_service'),
(61, 'Can add task', 16, 'add_task'),
(62, 'Can change task', 16, 'change_task'),
(63, 'Can delete task', 16, 'delete_task'),
(64, 'Can view task', 16, 'view_task'),
(65, 'Can add working hour', 17, 'add_workinghour'),
(66, 'Can change working hour', 17, 'change_workinghour'),
(67, 'Can delete working hour', 17, 'delete_workinghour'),
(68, 'Can view working hour', 17, 'view_workinghour'),
(69, 'Can add user service', 18, 'add_userservice'),
(70, 'Can change user service', 18, 'change_userservice'),
(71, 'Can delete user service', 18, 'delete_userservice'),
(72, 'Can view user service', 18, 'view_userservice'),
(73, 'Can add user blocked', 19, 'add_userblocked'),
(74, 'Can change user blocked', 19, 'change_userblocked'),
(75, 'Can delete user blocked', 19, 'delete_userblocked'),
(76, 'Can view user blocked', 19, 'view_userblocked'),
(77, 'Can add task edit', 20, 'add_taskedit'),
(78, 'Can change task edit', 20, 'change_taskedit'),
(79, 'Can delete task edit', 20, 'delete_taskedit'),
(80, 'Can view task edit', 20, 'view_taskedit'),
(81, 'Can add request edit', 21, 'add_requestedit'),
(82, 'Can change request edit', 21, 'change_requestedit'),
(83, 'Can delete request edit', 21, 'delete_requestedit'),
(84, 'Can view request edit', 21, 'view_requestedit'),
(85, 'Can add report', 22, 'add_report'),
(86, 'Can change report', 22, 'change_report'),
(87, 'Can delete report', 22, 'delete_report'),
(88, 'Can view report', 22, 'view_report'),
(89, 'Can add rating', 23, 'add_rating'),
(90, 'Can change rating', 23, 'change_rating'),
(91, 'Can delete rating', 23, 'delete_rating'),
(92, 'Can view rating', 23, 'view_rating'),
(93, 'Can add offer', 24, 'add_offer'),
(94, 'Can change offer', 24, 'change_offer'),
(95, 'Can delete offer', 24, 'delete_offer'),
(96, 'Can view offer', 24, 'view_offer'),
(97, 'Can add banned', 25, 'add_banned'),
(98, 'Can change banned', 25, 'change_banned'),
(99, 'Can delete banned', 25, 'delete_banned'),
(100, 'Can view banned', 25, 'view_banned'),
(101, 'Can add full request', 26, 'add_fullrequest'),
(102, 'Can change full request', 26, 'change_fullrequest'),
(103, 'Can delete full request', 26, 'delete_fullrequest'),
(104, 'Can view full request', 26, 'view_fullrequest'),
(105, 'Can add full user', 27, 'add_fulluser'),
(106, 'Can change full user', 27, 'change_fulluser'),
(107, 'Can delete full user', 27, 'delete_fulluser'),
(108, 'Can view full user', 27, 'view_fulluser'),
(109, 'Can add Token', 28, 'add_token'),
(110, 'Can change Token', 28, 'change_token'),
(111, 'Can delete Token', 28, 'delete_token'),
(112, 'Can view Token', 28, 'view_token');

-- --------------------------------------------------------

--
-- Table structure for table `django_admin_log`
--

CREATE TABLE `django_admin_log` (
  `id` int(11) NOT NULL,
  `action_time` datetime(6) NOT NULL,
  `object_id` longtext DEFAULT NULL,
  `object_repr` varchar(200) NOT NULL,
  `action_flag` smallint(5) UNSIGNED NOT NULL CHECK (`action_flag` >= 0),
  `change_message` longtext NOT NULL,
  `content_type_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `django_content_type`
--

CREATE TABLE `django_content_type` (
  `id` int(11) NOT NULL,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `django_content_type`
--

INSERT INTO `django_content_type` (`id`, `app_label`, `model`) VALUES
(1, 'admin', 'logentry'),
(7, 'api', 'achievement'),
(8, 'api', 'achievementlevel'),
(9, 'api', 'address'),
(25, 'api', 'banned'),
(10, 'api', 'benefit'),
(11, 'api', 'checklist'),
(26, 'api', 'fullrequest'),
(27, 'api', 'fulluser'),
(12, 'api', 'notification'),
(24, 'api', 'offer'),
(13, 'api', 'picture'),
(23, 'api', 'rating'),
(22, 'api', 'report'),
(14, 'api', 'request'),
(21, 'api', 'requestedit'),
(15, 'api', 'service'),
(16, 'api', 'task'),
(20, 'api', 'taskedit'),
(6, 'api', 'user'),
(19, 'api', 'userblocked'),
(18, 'api', 'userservice'),
(17, 'api', 'workinghour'),
(3, 'auth', 'group'),
(2, 'auth', 'permission'),
(28, 'authtoken', 'token'),
(4, 'contenttypes', 'contenttype'),
(5, 'sessions', 'session');

-- --------------------------------------------------------

--
-- Table structure for table `django_migrations`
--

CREATE TABLE `django_migrations` (
  `id` int(11) NOT NULL,
  `app` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `applied` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `django_migrations`
--

INSERT INTO `django_migrations` (`id`, `app`, `name`, `applied`) VALUES
(1, 'contenttypes', '0001_initial', '2020-03-18 21:52:50.565286'),
(2, 'contenttypes', '0002_remove_content_type_name', '2020-03-18 21:52:50.698973'),
(3, 'auth', '0001_initial', '2020-03-18 21:52:50.832417'),
(4, 'auth', '0002_alter_permission_name_max_length', '2020-03-18 21:52:51.287272'),
(5, 'auth', '0003_alter_user_email_max_length', '2020-03-18 21:52:51.309078'),
(6, 'auth', '0004_alter_user_username_opts', '2020-03-18 21:52:51.338205'),
(7, 'auth', '0005_alter_user_last_login_null', '2020-03-18 21:52:51.358166'),
(8, 'auth', '0006_require_contenttypes_0002', '2020-03-18 21:52:51.371875'),
(9, 'auth', '0007_alter_validators_add_error_messages', '2020-03-18 21:52:51.403422'),
(10, 'auth', '0008_alter_user_username_max_length', '2020-03-18 21:52:51.416275'),
(11, 'auth', '0009_alter_user_last_name_max_length', '2020-03-18 21:52:51.424196'),
(12, 'auth', '0010_alter_group_name_max_length', '2020-03-18 21:52:51.549587'),
(13, 'auth', '0011_update_proxy_permissions', '2020-03-18 21:52:51.560927'),
(14, 'api', '0001_initial', '2020-03-18 21:52:54.175574'),
(15, 'admin', '0001_initial', '2020-03-18 21:53:01.590179'),
(16, 'admin', '0002_logentry_remove_auto_add', '2020-03-18 21:53:01.825683'),
(17, 'admin', '0003_logentry_add_action_flag_choices', '2020-03-18 21:53:01.872672'),
(18, 'api', '0002_auto_20200312_2150', '2020-03-18 21:53:02.841493'),
(19, 'api', '0003_auto_20200318_2150', '2020-03-18 21:53:04.885992'),
(20, 'api', '0004_auto_20200318_2152', '2020-03-18 21:53:08.421589'),
(21, 'authtoken', '0001_initial', '2020-03-18 21:53:08.594280'),
(22, 'authtoken', '0002_auto_20160226_1747', '2020-03-18 21:53:09.047280'),
(23, 'sessions', '0001_initial', '2020-03-18 21:53:09.112668'),
(24, 'api', '0005_delete_userblocked', '2020-03-18 22:55:22.674578'),
(25, 'api', '0006_auto_20200319_1252', '2020-03-19 12:52:42.539319'),
(26, 'api', '0007_request_tasklist', '2020-03-19 13:35:13.064847');

-- --------------------------------------------------------

--
-- Table structure for table `django_session`
--

CREATE TABLE `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `api_achievement`
--
ALTER TABLE `api_achievement`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_achievementlevel`
--
ALTER TABLE `api_achievementlevel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_achievementlevel_achievement_id_33763cde_fk_api_achie` (`achievement_id`),
  ADD KEY `api_achievementlevel_user_id_20b80cc2_fk_api_user_id` (`user_id`);

--
-- Indexes for table `api_address`
--
ALTER TABLE `api_address`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_banned`
--
ALTER TABLE `api_banned`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_banned_banned_user_id_c09fb729_fk_api_user_id` (`banned_user_id`);

--
-- Indexes for table `api_benefit`
--
ALTER TABLE `api_benefit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_benefit_benefit_user_id_09c48aa1_fk_api_user_id` (`benefit_user_id`);

--
-- Indexes for table `api_checklist`
--
ALTER TABLE `api_checklist`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_fullrequest`
--
ALTER TABLE `api_fullrequest`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_fullrequest_accepted_offer_id_cfbbf8d2_fk_api_offer_id` (`accepted_offer_id`),
  ADD KEY `api_fullrequest_rating_id_90bc8475_fk_api_rating_id` (`rating_id`),
  ADD KEY `api_fullrequest_request_id_fbf6bbdb_fk_api_request_id` (`request_id`);

--
-- Indexes for table `api_fullrequest_offers`
--
ALTER TABLE `api_fullrequest_offers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fullrequest_offers_fullrequest_id_offer_id_bb76df84_uniq` (`fullrequest_id`,`offer_id`),
  ADD KEY `api_fullrequest_offers_offer_id_31486bf7_fk_api_offer_id` (`offer_id`);

--
-- Indexes for table `api_fulluser`
--
ALTER TABLE `api_fulluser`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_fulluser_user_id_496eef57_fk_api_user_id` (`user_id`);

--
-- Indexes for table `api_fulluser_achievements`
--
ALTER TABLE `api_fulluser_achievements`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_achievement_fulluser_id_achievementl_4a674768_uniq` (`fulluser_id`,`achievementlevel_id`),
  ADD KEY `api_fulluser_achieve_achievementlevel_id_9fe0b773_fk_api_achie` (`achievementlevel_id`);

--
-- Indexes for table `api_fulluser_addresses`
--
ALTER TABLE `api_fulluser_addresses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_addresses_fulluser_id_address_id_888db9f0_uniq` (`fulluser_id`,`address_id`),
  ADD KEY `api_fulluser_addresses_address_id_232c5ef4_fk_api_address_id` (`address_id`);

--
-- Indexes for table `api_fulluser_benefitlist`
--
ALTER TABLE `api_fulluser_benefitlist`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_benefitlist_fulluser_id_benefit_id_53df8dfe_uniq` (`fulluser_id`,`benefit_id`),
  ADD KEY `api_fulluser_benefitlist_benefit_id_838283b3_fk_api_benefit_id` (`benefit_id`);

--
-- Indexes for table `api_fulluser_blocked`
--
ALTER TABLE `api_fulluser_blocked`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_blocked_fulluser_id_user_id_1c9d2f36_uniq` (`fulluser_id`,`user_id`),
  ADD KEY `api_fulluser_blocked_user_id_ad214590_fk_api_user_id` (`user_id`);

--
-- Indexes for table `api_fulluser_notifications`
--
ALTER TABLE `api_fulluser_notifications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_notificatio_fulluser_id_notification_eb057410_uniq` (`fulluser_id`,`notification_id`),
  ADD KEY `api_fulluser_notific_notification_id_4a7f71c8_fk_api_notif` (`notification_id`);

--
-- Indexes for table `api_fulluser_offers`
--
ALTER TABLE `api_fulluser_offers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_offers_fulluser_id_offer_id_775f5ff2_uniq` (`fulluser_id`,`offer_id`),
  ADD KEY `api_fulluser_offers_offer_id_df678a13_fk_api_offer_id` (`offer_id`);

--
-- Indexes for table `api_fulluser_ratings`
--
ALTER TABLE `api_fulluser_ratings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_ratings_fulluser_id_rating_id_0ca16dd1_uniq` (`fulluser_id`,`rating_id`),
  ADD KEY `api_fulluser_ratings_rating_id_4f272a4c_fk_api_rating_id` (`rating_id`);

--
-- Indexes for table `api_fulluser_requests`
--
ALTER TABLE `api_fulluser_requests`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_requests_fulluser_id_request_id_85f317d3_uniq` (`fulluser_id`,`request_id`),
  ADD KEY `api_fulluser_requests_request_id_dcd457b3_fk_api_request_id` (`request_id`);

--
-- Indexes for table `api_fulluser_services`
--
ALTER TABLE `api_fulluser_services`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_services_fulluser_id_userservice_id_e07d7c3a_uniq` (`fulluser_id`,`userservice_id`),
  ADD KEY `api_fulluser_service_userservice_id_0f6621fe_fk_api_users` (`userservice_id`);

--
-- Indexes for table `api_fulluser_working_hours`
--
ALTER TABLE `api_fulluser_working_hours`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_fulluser_working_hou_fulluser_id_workinghour__2df7a4a7_uniq` (`fulluser_id`,`workinghour_id`),
  ADD KEY `api_fulluser_working_workinghour_id_4d26f9fc_fk_api_worki` (`workinghour_id`);

--
-- Indexes for table `api_notification`
--
ALTER TABLE `api_notification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_offer`
--
ALTER TABLE `api_offer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_offer_created_by_id_97a1c310_fk_api_user_id` (`created_by_id`),
  ADD KEY `api_offer_edit_id_68602fd8_fk_api_requestedit_id` (`edit_id`),
  ADD KEY `api_offer_request_id_7de4c887_fk_api_request_id` (`request_id`);

--
-- Indexes for table `api_picture`
--
ALTER TABLE `api_picture`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_rating`
--
ALTER TABLE `api_rating`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_rating_created_by_id_a5dfb1f4_fk_api_user_id` (`created_by_id`),
  ADD KEY `api_rating_rated_user_id_9c5708cb_fk_api_user_id` (`rated_user_id`),
  ADD KEY `api_rating_request_id_5266454d_fk_api_request_id` (`request_id`);

--
-- Indexes for table `api_report`
--
ALTER TABLE `api_report`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_report_created_by_id_930926a4_fk_api_user_id` (`created_by_id`),
  ADD KEY `api_report_reported_user_id_8e970d74_fk_api_user_id` (`reported_user_id`);

--
-- Indexes for table `api_request`
--
ALTER TABLE `api_request`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_request_service_type_id_c364e70b_fk_api_service_id` (`service_type_id`),
  ADD KEY `api_request_created_by_id_0c7d7077_fk_api_user_id` (`created_by_id`);

--
-- Indexes for table `api_requestedit`
--
ALTER TABLE `api_requestedit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_requestedit_request`
--
ALTER TABLE `api_requestedit_request`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_requestedit_request_requestedit_id_request_id_9c1ae2e5_uniq` (`requestedit_id`,`request_id`),
  ADD KEY `api_requestedit_request_request_id_ed0425e7_fk_api_request_id` (`request_id`);

--
-- Indexes for table `api_requestedit_tasks`
--
ALTER TABLE `api_requestedit_tasks`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_requestedit_tasks_requestedit_id_taskedit_id_244f9447_uniq` (`requestedit_id`,`taskedit_id`),
  ADD KEY `api_requestedit_tasks_taskedit_id_58c33f07_fk_api_taskedit_id` (`taskedit_id`);

--
-- Indexes for table `api_request_pictures`
--
ALTER TABLE `api_request_pictures`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_request_pictures_request_id_picture_id_e55855c5_uniq` (`request_id`,`picture_id`),
  ADD KEY `api_request_pictures_picture_id_917c777a_fk_api_picture_id` (`picture_id`);

--
-- Indexes for table `api_request_tasklist`
--
ALTER TABLE `api_request_tasklist`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_request_tasklist_request_id_task_id_37d4a995_uniq` (`request_id`,`task_id`),
  ADD KEY `api_request_tasklist_task_id_8a7c0258_fk_api_task_id` (`task_id`);

--
-- Indexes for table `api_service`
--
ALTER TABLE `api_service`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `api_task`
--
ALTER TABLE `api_task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_task_address_id_30b75c6d_fk_api_address_id` (`address_id`),
  ADD KEY `api_task_service_type_id_205a35ee_fk_api_service_id` (`service_type_id`);

--
-- Indexes for table `api_taskedit`
--
ALTER TABLE `api_taskedit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_taskedit_address_id_518487e1_fk_api_address_id` (`address_id`),
  ADD KEY `api_taskedit_task_id_af1df043_fk_api_task_id` (`task_id`);

--
-- Indexes for table `api_task_checklist`
--
ALTER TABLE `api_task_checklist`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_task_checklist_task_id_checklist_id_7f3b2d9e_uniq` (`task_id`,`checklist_id`),
  ADD KEY `api_task_checklist_checklist_id_c3d5f2ec_fk_api_checklist_id` (`checklist_id`);

--
-- Indexes for table `api_task_pictures`
--
ALTER TABLE `api_task_pictures`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_task_pictures_task_id_picture_id_ce101542_uniq` (`task_id`,`picture_id`),
  ADD KEY `api_task_pictures_picture_id_2d53974a_fk_api_picture_id` (`picture_id`);

--
-- Indexes for table `api_user`
--
ALTER TABLE `api_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `api_userservice`
--
ALTER TABLE `api_userservice`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_userservice_service_id_4ed3a1e1_fk_api_service_id` (`service_id`);

--
-- Indexes for table `api_user_groups`
--
ALTER TABLE `api_user_groups`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_user_groups_user_id_group_id_9c7ddfb5_uniq` (`user_id`,`group_id`),
  ADD KEY `api_user_groups_group_id_3af85785_fk_auth_group_id` (`group_id`);

--
-- Indexes for table `api_user_user_permissions`
--
ALTER TABLE `api_user_user_permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `api_user_user_permissions_user_id_permission_id_a06dd704_uniq` (`user_id`,`permission_id`),
  ADD KEY `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` (`permission_id`);

--
-- Indexes for table `api_workinghour`
--
ALTER TABLE `api_workinghour`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `authtoken_token`
--
ALTER TABLE `authtoken_token`
  ADD PRIMARY KEY (`key`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `auth_group`
--
ALTER TABLE `auth_group`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `auth_group_permissions`
--
ALTER TABLE `auth_group_permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `auth_group_permissions_group_id_permission_id_0cd325b0_uniq` (`group_id`,`permission_id`),
  ADD KEY `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` (`permission_id`);

--
-- Indexes for table `auth_permission`
--
ALTER TABLE `auth_permission`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `auth_permission_content_type_id_codename_01ab375a_uniq` (`content_type_id`,`codename`);

--
-- Indexes for table `django_admin_log`
--
ALTER TABLE `django_admin_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `django_admin_log_content_type_id_c4bce8eb_fk_django_co` (`content_type_id`),
  ADD KEY `django_admin_log_user_id_c564eba6_fk_api_user_id` (`user_id`);

--
-- Indexes for table `django_content_type`
--
ALTER TABLE `django_content_type`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `django_content_type_app_label_model_76bd3d3b_uniq` (`app_label`,`model`);

--
-- Indexes for table `django_migrations`
--
ALTER TABLE `django_migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `django_session`
--
ALTER TABLE `django_session`
  ADD PRIMARY KEY (`session_key`),
  ADD KEY `django_session_expire_date_a5c62663` (`expire_date`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `api_achievement`
--
ALTER TABLE `api_achievement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `api_achievementlevel`
--
ALTER TABLE `api_achievementlevel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_address`
--
ALTER TABLE `api_address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `api_banned`
--
ALTER TABLE `api_banned`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_benefit`
--
ALTER TABLE `api_benefit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `api_checklist`
--
ALTER TABLE `api_checklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `api_fullrequest`
--
ALTER TABLE `api_fullrequest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_fullrequest_offers`
--
ALTER TABLE `api_fullrequest_offers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser`
--
ALTER TABLE `api_fulluser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `api_fulluser_achievements`
--
ALTER TABLE `api_fulluser_achievements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser_addresses`
--
ALTER TABLE `api_fulluser_addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `api_fulluser_benefitlist`
--
ALTER TABLE `api_fulluser_benefitlist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `api_fulluser_blocked`
--
ALTER TABLE `api_fulluser_blocked`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `api_fulluser_notifications`
--
ALTER TABLE `api_fulluser_notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser_offers`
--
ALTER TABLE `api_fulluser_offers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser_ratings`
--
ALTER TABLE `api_fulluser_ratings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser_requests`
--
ALTER TABLE `api_fulluser_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_fulluser_services`
--
ALTER TABLE `api_fulluser_services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `api_fulluser_working_hours`
--
ALTER TABLE `api_fulluser_working_hours`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `api_notification`
--
ALTER TABLE `api_notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_offer`
--
ALTER TABLE `api_offer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_picture`
--
ALTER TABLE `api_picture`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_rating`
--
ALTER TABLE `api_rating`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_report`
--
ALTER TABLE `api_report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_request`
--
ALTER TABLE `api_request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_requestedit`
--
ALTER TABLE `api_requestedit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_requestedit_request`
--
ALTER TABLE `api_requestedit_request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_requestedit_tasks`
--
ALTER TABLE `api_requestedit_tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_request_pictures`
--
ALTER TABLE `api_request_pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_request_tasklist`
--
ALTER TABLE `api_request_tasklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_service`
--
ALTER TABLE `api_service`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `api_task`
--
ALTER TABLE `api_task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_taskedit`
--
ALTER TABLE `api_taskedit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_task_checklist`
--
ALTER TABLE `api_task_checklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `api_task_pictures`
--
ALTER TABLE `api_task_pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_user`
--
ALTER TABLE `api_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `api_userservice`
--
ALTER TABLE `api_userservice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `api_user_groups`
--
ALTER TABLE `api_user_groups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_user_user_permissions`
--
ALTER TABLE `api_user_user_permissions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_workinghour`
--
ALTER TABLE `api_workinghour`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `auth_group`
--
ALTER TABLE `auth_group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `auth_group_permissions`
--
ALTER TABLE `auth_group_permissions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `auth_permission`
--
ALTER TABLE `auth_permission`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- AUTO_INCREMENT for table `django_admin_log`
--
ALTER TABLE `django_admin_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `django_content_type`
--
ALTER TABLE `django_content_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `django_migrations`
--
ALTER TABLE `django_migrations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `api_achievementlevel`
--
ALTER TABLE `api_achievementlevel`
  ADD CONSTRAINT `api_achievementlevel_achievement_id_33763cde_fk_api_achie` FOREIGN KEY (`achievement_id`) REFERENCES `api_achievement` (`id`),
  ADD CONSTRAINT `api_achievementlevel_user_id_20b80cc2_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_banned`
--
ALTER TABLE `api_banned`
  ADD CONSTRAINT `api_banned_banned_user_id_c09fb729_fk_api_user_id` FOREIGN KEY (`banned_user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_benefit`
--
ALTER TABLE `api_benefit`
  ADD CONSTRAINT `api_benefit_benefit_user_id_09c48aa1_fk_api_user_id` FOREIGN KEY (`benefit_user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_fullrequest`
--
ALTER TABLE `api_fullrequest`
  ADD CONSTRAINT `api_fullrequest_accepted_offer_id_cfbbf8d2_fk_api_offer_id` FOREIGN KEY (`accepted_offer_id`) REFERENCES `api_offer` (`id`),
  ADD CONSTRAINT `api_fullrequest_rating_id_90bc8475_fk_api_rating_id` FOREIGN KEY (`rating_id`) REFERENCES `api_rating` (`id`),
  ADD CONSTRAINT `api_fullrequest_request_id_fbf6bbdb_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_fullrequest_offers`
--
ALTER TABLE `api_fullrequest_offers`
  ADD CONSTRAINT `api_fullrequest_offe_fullrequest_id_ce6c3e6e_fk_api_fullr` FOREIGN KEY (`fullrequest_id`) REFERENCES `api_fullrequest` (`id`),
  ADD CONSTRAINT `api_fullrequest_offers_offer_id_31486bf7_fk_api_offer_id` FOREIGN KEY (`offer_id`) REFERENCES `api_offer` (`id`);

--
-- Constraints for table `api_fulluser`
--
ALTER TABLE `api_fulluser`
  ADD CONSTRAINT `api_fulluser_user_id_496eef57_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_fulluser_achievements`
--
ALTER TABLE `api_fulluser_achievements`
  ADD CONSTRAINT `api_fulluser_achieve_achievementlevel_id_9fe0b773_fk_api_achie` FOREIGN KEY (`achievementlevel_id`) REFERENCES `api_achievementlevel` (`id`),
  ADD CONSTRAINT `api_fulluser_achieve_fulluser_id_22559295_fk_api_fullu` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`);

--
-- Constraints for table `api_fulluser_addresses`
--
ALTER TABLE `api_fulluser_addresses`
  ADD CONSTRAINT `api_fulluser_addresses_address_id_232c5ef4_fk_api_address_id` FOREIGN KEY (`address_id`) REFERENCES `api_address` (`id`),
  ADD CONSTRAINT `api_fulluser_addresses_fulluser_id_088e862c_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`);

--
-- Constraints for table `api_fulluser_benefitlist`
--
ALTER TABLE `api_fulluser_benefitlist`
  ADD CONSTRAINT `api_fulluser_benefitlist_benefit_id_838283b3_fk_api_benefit_id` FOREIGN KEY (`benefit_id`) REFERENCES `api_benefit` (`id`),
  ADD CONSTRAINT `api_fulluser_benefitlist_fulluser_id_6b9df14e_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`);

--
-- Constraints for table `api_fulluser_blocked`
--
ALTER TABLE `api_fulluser_blocked`
  ADD CONSTRAINT `api_fulluser_blocked_fulluser_id_7f6fde58_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_blocked_user_id_ad214590_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_fulluser_notifications`
--
ALTER TABLE `api_fulluser_notifications`
  ADD CONSTRAINT `api_fulluser_notific_fulluser_id_b1f9b811_fk_api_fullu` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_notific_notification_id_4a7f71c8_fk_api_notif` FOREIGN KEY (`notification_id`) REFERENCES `api_notification` (`id`);

--
-- Constraints for table `api_fulluser_offers`
--
ALTER TABLE `api_fulluser_offers`
  ADD CONSTRAINT `api_fulluser_offers_fulluser_id_946b6a06_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_offers_offer_id_df678a13_fk_api_offer_id` FOREIGN KEY (`offer_id`) REFERENCES `api_offer` (`id`);

--
-- Constraints for table `api_fulluser_ratings`
--
ALTER TABLE `api_fulluser_ratings`
  ADD CONSTRAINT `api_fulluser_ratings_fulluser_id_3c81b6e0_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_ratings_rating_id_4f272a4c_fk_api_rating_id` FOREIGN KEY (`rating_id`) REFERENCES `api_rating` (`id`);

--
-- Constraints for table `api_fulluser_requests`
--
ALTER TABLE `api_fulluser_requests`
  ADD CONSTRAINT `api_fulluser_requests_fulluser_id_7eeee536_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_requests_request_id_dcd457b3_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_fulluser_services`
--
ALTER TABLE `api_fulluser_services`
  ADD CONSTRAINT `api_fulluser_service_userservice_id_0f6621fe_fk_api_users` FOREIGN KEY (`userservice_id`) REFERENCES `api_userservice` (`id`),
  ADD CONSTRAINT `api_fulluser_services_fulluser_id_4a9e5fd0_fk_api_fulluser_id` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`);

--
-- Constraints for table `api_fulluser_working_hours`
--
ALTER TABLE `api_fulluser_working_hours`
  ADD CONSTRAINT `api_fulluser_working_fulluser_id_759e19c4_fk_api_fullu` FOREIGN KEY (`fulluser_id`) REFERENCES `api_fulluser` (`id`),
  ADD CONSTRAINT `api_fulluser_working_workinghour_id_4d26f9fc_fk_api_worki` FOREIGN KEY (`workinghour_id`) REFERENCES `api_workinghour` (`id`);

--
-- Constraints for table `api_offer`
--
ALTER TABLE `api_offer`
  ADD CONSTRAINT `api_offer_created_by_id_97a1c310_fk_api_user_id` FOREIGN KEY (`created_by_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_offer_edit_id_68602fd8_fk_api_requestedit_id` FOREIGN KEY (`edit_id`) REFERENCES `api_requestedit` (`id`),
  ADD CONSTRAINT `api_offer_request_id_7de4c887_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_rating`
--
ALTER TABLE `api_rating`
  ADD CONSTRAINT `api_rating_created_by_id_a5dfb1f4_fk_api_user_id` FOREIGN KEY (`created_by_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_rating_rated_user_id_9c5708cb_fk_api_user_id` FOREIGN KEY (`rated_user_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_rating_request_id_5266454d_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_report`
--
ALTER TABLE `api_report`
  ADD CONSTRAINT `api_report_created_by_id_930926a4_fk_api_user_id` FOREIGN KEY (`created_by_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_report_reported_user_id_8e970d74_fk_api_user_id` FOREIGN KEY (`reported_user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_request`
--
ALTER TABLE `api_request`
  ADD CONSTRAINT `api_request_created_by_id_0c7d7077_fk_api_user_id` FOREIGN KEY (`created_by_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_request_service_type_id_c364e70b_fk_api_service_id` FOREIGN KEY (`service_type_id`) REFERENCES `api_service` (`id`);

--
-- Constraints for table `api_requestedit_request`
--
ALTER TABLE `api_requestedit_request`
  ADD CONSTRAINT `api_requestedit_requ_requestedit_id_b864f97f_fk_api_reque` FOREIGN KEY (`requestedit_id`) REFERENCES `api_requestedit` (`id`),
  ADD CONSTRAINT `api_requestedit_request_request_id_ed0425e7_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_requestedit_tasks`
--
ALTER TABLE `api_requestedit_tasks`
  ADD CONSTRAINT `api_requestedit_task_requestedit_id_b5390025_fk_api_reque` FOREIGN KEY (`requestedit_id`) REFERENCES `api_requestedit` (`id`),
  ADD CONSTRAINT `api_requestedit_tasks_taskedit_id_58c33f07_fk_api_taskedit_id` FOREIGN KEY (`taskedit_id`) REFERENCES `api_taskedit` (`id`);

--
-- Constraints for table `api_request_pictures`
--
ALTER TABLE `api_request_pictures`
  ADD CONSTRAINT `api_request_pictures_picture_id_917c777a_fk_api_picture_id` FOREIGN KEY (`picture_id`) REFERENCES `api_picture` (`id`),
  ADD CONSTRAINT `api_request_pictures_request_id_feb01a01_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

--
-- Constraints for table `api_request_tasklist`
--
ALTER TABLE `api_request_tasklist`
  ADD CONSTRAINT `api_request_tasklist_request_id_5873bb5a_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`),
  ADD CONSTRAINT `api_request_tasklist_task_id_8a7c0258_fk_api_task_id` FOREIGN KEY (`task_id`) REFERENCES `api_task` (`id`);

--
-- Constraints for table `api_task`
--
ALTER TABLE `api_task`
  ADD CONSTRAINT `api_task_address_id_30b75c6d_fk_api_address_id` FOREIGN KEY (`address_id`) REFERENCES `api_address` (`id`),
  ADD CONSTRAINT `api_task_service_type_id_205a35ee_fk_api_service_id` FOREIGN KEY (`service_type_id`) REFERENCES `api_service` (`id`);

--
-- Constraints for table `api_taskedit`
--
ALTER TABLE `api_taskedit`
  ADD CONSTRAINT `api_taskedit_address_id_518487e1_fk_api_address_id` FOREIGN KEY (`address_id`) REFERENCES `api_address` (`id`),
  ADD CONSTRAINT `api_taskedit_task_id_af1df043_fk_api_task_id` FOREIGN KEY (`task_id`) REFERENCES `api_task` (`id`);

--
-- Constraints for table `api_task_checklist`
--
ALTER TABLE `api_task_checklist`
  ADD CONSTRAINT `api_task_checklist_checklist_id_c3d5f2ec_fk_api_checklist_id` FOREIGN KEY (`checklist_id`) REFERENCES `api_checklist` (`id`),
  ADD CONSTRAINT `api_task_checklist_task_id_f7c1f23b_fk_api_task_id` FOREIGN KEY (`task_id`) REFERENCES `api_task` (`id`);

--
-- Constraints for table `api_task_pictures`
--
ALTER TABLE `api_task_pictures`
  ADD CONSTRAINT `api_task_pictures_picture_id_2d53974a_fk_api_picture_id` FOREIGN KEY (`picture_id`) REFERENCES `api_picture` (`id`),
  ADD CONSTRAINT `api_task_pictures_task_id_31eb71de_fk_api_task_id` FOREIGN KEY (`task_id`) REFERENCES `api_task` (`id`);

--
-- Constraints for table `api_userservice`
--
ALTER TABLE `api_userservice`
  ADD CONSTRAINT `api_userservice_service_id_4ed3a1e1_fk_api_service_id` FOREIGN KEY (`service_id`) REFERENCES `api_service` (`id`);

--
-- Constraints for table `api_user_groups`
--
ALTER TABLE `api_user_groups`
  ADD CONSTRAINT `api_user_groups_group_id_3af85785_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  ADD CONSTRAINT `api_user_groups_user_id_a5ff39fa_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_user_user_permissions`
--
ALTER TABLE `api_user_user_permissions`
  ADD CONSTRAINT `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  ADD CONSTRAINT `api_user_user_permissions_user_id_f3945d65_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `authtoken_token`
--
ALTER TABLE `authtoken_token`
  ADD CONSTRAINT `authtoken_token_user_id_35299eff_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `auth_group_permissions`
--
ALTER TABLE `auth_group_permissions`
  ADD CONSTRAINT `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  ADD CONSTRAINT `auth_group_permissions_group_id_b120cbf9_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`);

--
-- Constraints for table `auth_permission`
--
ALTER TABLE `auth_permission`
  ADD CONSTRAINT `auth_permission_content_type_id_2f476e4b_fk_django_co` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);

--
-- Constraints for table `django_admin_log`
--
ALTER TABLE `django_admin_log`
  ADD CONSTRAINT `django_admin_log_content_type_id_c4bce8eb_fk_django_co` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`),
  ADD CONSTRAINT `django_admin_log_user_id_c564eba6_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
