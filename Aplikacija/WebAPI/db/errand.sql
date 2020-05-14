-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 14, 2020 at 09:23 PM
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
  `name_sr` varchar(50) NOT NULL,
  `name_en` varchar(50) NOT NULL,
  `description_sr` varchar(256) NOT NULL,
  `description_en` varchar(256) NOT NULL,
  `icon` varchar(100) NOT NULL,
  `requirements` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_achievement`
--

INSERT INTO `api_achievement` (`id`, `name_sr`, `name_en`, `description_sr`, `description_en`, `icon`, `requirements`) VALUES
(1, 'Eko korisnik', 'Eco user', 'Usluge izvrsava bez koriscenja motornih vozila', 'Services done without motor vehicle', 'db/images/achievements/1.png', 'Pseudo SQL query?'),
(2, 'Savrsen korisnik', 'Perfect user', 'Sve zahteve obavio sa maksimalnom ocenom', 'All requests done with maximum rating', 'db/images/achievements/2.png', 'Pseudo SQL query?');

-- --------------------------------------------------------

--
-- Table structure for table `api_achievementlevel`
--

CREATE TABLE `api_achievementlevel` (
  `id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_address`
--

CREATE TABLE `api_address` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `home` tinyint(1) NOT NULL,
  `arrived` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_address`
--

INSERT INTO `api_address` (`id`, `name`, `longitude`, `latitude`, `home`, `arrived`) VALUES
(1, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 1, 0),
(2, 'Dragise Cvetkovica 52', 43.319187, 21.913393, 1, 0),
(3, 'Dragise Cvetkovica 12', 43.31918, 21.913393, 0, 0),
(4, 'Dragise Cvetkovica 18', 43.319182, 21.913393, 1, 0),
(6, 'Dragise Cvetkovica 60', 43.31919, 21.913399, 0, 0),
(7, 'Dragise Cvetkovica 22', 43.319185, 21.913395, 1, 0),
(8, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 1, 0),
(9, 'Dragise Cvetkovica 22', 43.319185, 21.913395, 1, 0),
(10, 'Dragise Cvetkovica 42', 43.319185, 21.913395, 1, 0),
(11, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(12, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(13, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(14, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(15, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(16, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(17, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(18, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(19, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(20, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(21, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(22, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(23, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(24, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(25, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(26, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(27, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(28, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(29, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(30, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(31, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(32, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(33, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0),
(34, 'Dragise Cvetkovica 32', 43.319185, 21.913395, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `api_banned`
--

CREATE TABLE `api_banned` (
  `id` int(11) NOT NULL,
  `until` datetime(6) NOT NULL,
  `comment` varchar(256) NOT NULL,
  `banned_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_benefit`
--

CREATE TABLE `api_benefit` (
  `id` int(11) NOT NULL,
  `discount` double NOT NULL,
  `benefit_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_benefit`
--

INSERT INTO `api_benefit` (`id`, `discount`, `benefit_user_id`) VALUES
(1, 0.3, 3),
(2, 0.2, 4),
(4, 0.1, 2),
(5, 0.2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_checklist`
--

CREATE TABLE `api_checklist` (
  `id` int(11) NOT NULL,
  `check_list` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_checklist`
--

INSERT INTO `api_checklist` (`id`, `check_list`) VALUES
(1, 'item1'),
(2, 'item2'),
(3, 'i3'),
(4, 'i4'),
(5, 'item1'),
(6, 'item2'),
(7, 'racun1'),
(8, 'racun2');

-- --------------------------------------------------------

--
-- Table structure for table `api_fullrequest`
--

CREATE TABLE `api_fullrequest` (
  `id` int(11) NOT NULL,
  `accepted_offer_id` int(11) DEFAULT NULL,
  `rating_created_by_id` int(11) DEFAULT NULL,
  `rating_working_with_id` int(11) DEFAULT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fullrequest`
--

INSERT INTO `api_fullrequest` (`id`, `accepted_offer_id`, `rating_created_by_id`, `rating_working_with_id`, `request_id`) VALUES
(1, 23, NULL, NULL, 1),
(2, NULL, NULL, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `api_fullrequest_offers`
--

CREATE TABLE `api_fullrequest_offers` (
  `id` int(11) NOT NULL,
  `fullrequest_id` int(11) NOT NULL,
  `offer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser`
--

CREATE TABLE `api_fulluser` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser`
--

INSERT INTO `api_fulluser` (`id`, `user_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_achievements`
--

CREATE TABLE `api_fulluser_achievements` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `achievementlevel_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_addresses`
--

CREATE TABLE `api_fulluser_addresses` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_addresses`
--

INSERT INTO `api_fulluser_addresses` (`id`, `fulluser_id`, `address_id`) VALUES
(1, 2, 1),
(2, 3, 2),
(3, 4, 3),
(4, 5, 4),
(6, 5, 6);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_benefitlist`
--

CREATE TABLE `api_fulluser_benefitlist` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `benefit_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_benefitlist`
--

INSERT INTO `api_fulluser_benefitlist` (`id`, `fulluser_id`, `benefit_id`) VALUES
(1, 2, 1),
(2, 2, 2),
(4, 5, 4),
(5, 5, 5);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_blocked`
--

CREATE TABLE `api_fulluser_blocked` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_blocked`
--

INSERT INTO `api_fulluser_blocked` (`id`, `fulluser_id`, `user_id`) VALUES
(2, 3, 4);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_notifications`
--

CREATE TABLE `api_fulluser_notifications` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_offers`
--

CREATE TABLE `api_fulluser_offers` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `offer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_offers`
--

INSERT INTO `api_fulluser_offers` (`id`, `fulluser_id`, `offer_id`) VALUES
(1, 2, 1),
(2, 5, 2),
(4, 5, 4),
(5, 5, 5),
(6, 5, 6),
(7, 5, 7),
(8, 5, 8),
(9, 5, 9),
(10, 5, 10),
(11, 5, 11),
(12, 5, 12),
(13, 5, 13),
(14, 5, 14),
(15, 5, 15),
(16, 5, 16),
(17, 5, 17),
(18, 5, 18),
(19, 5, 19),
(20, 5, 20),
(21, 5, 21),
(22, 5, 22),
(23, 5, 23),
(24, 5, 24);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_ratings`
--

CREATE TABLE `api_fulluser_ratings` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `rating_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_requests`
--

CREATE TABLE `api_fulluser_requests` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_requests`
--

INSERT INTO `api_fulluser_requests` (`id`, `fulluser_id`, `request_id`) VALUES
(1, 4, 1),
(2, 4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_services`
--

CREATE TABLE `api_fulluser_services` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `userservice_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_services`
--

INSERT INTO `api_fulluser_services` (`id`, `fulluser_id`, `userservice_id`) VALUES
(1, 2, 1),
(2, 2, 2),
(6, 3, 6),
(3, 4, 3),
(4, 4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `api_fulluser_working_hours`
--

CREATE TABLE `api_fulluser_working_hours` (
  `id` int(11) NOT NULL,
  `fulluser_id` int(11) NOT NULL,
  `workinghour_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_fulluser_working_hours`
--

INSERT INTO `api_fulluser_working_hours` (`id`, `fulluser_id`, `workinghour_id`) VALUES
(1, 2, 1),
(2, 2, 2),
(3, 2, 3),
(4, 2, 4),
(5, 2, 5),
(6, 3, 6),
(7, 3, 7),
(8, 3, 8),
(9, 3, 9),
(10, 3, 10),
(11, 4, 11),
(12, 4, 12),
(13, 4, 13),
(14, 5, 14),
(15, 5, 15),
(17, 5, 17);

-- --------------------------------------------------------

--
-- Table structure for table `api_location`
--

CREATE TABLE `api_location` (
  `id` int(11) NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_offer`
--

INSERT INTO `api_offer` (`id`, `payment_type`, `payment_ammount`, `created_by_id`, `edit_id`, `request_id`) VALUES
(1, '1', 500, 2, 1, 1),
(2, '1', 600, 5, 2, 1),
(4, '1', 600, 5, 4, 1),
(5, '1', 600, 5, 5, 1),
(6, '1', 600, 5, 6, 1),
(7, '1', 600, 5, 7, 1),
(8, '1', 600, 5, 8, 1),
(9, '1', 600, 5, 9, 1),
(10, '1', 600, 5, 10, 1),
(11, '1', 600, 5, 11, 1),
(12, '1', 600, 5, 12, 1),
(13, '1', 600, 5, 13, 1),
(14, '1', 600, 5, 14, 1),
(15, '1', 600, 5, 15, 1),
(16, '1', 600, 5, 16, 1),
(17, '1', 600, 5, 17, 1),
(18, '1', 600, 5, 18, 1),
(19, '1', 600, 5, 19, 1),
(20, '1', 600, 5, 20, 1),
(21, '1', 600, 5, 21, 1),
(22, '1', 600, 5, 22, 1),
(23, '1', 600, 5, 23, 1),
(24, '1', 600, 5, 24, 1);

-- --------------------------------------------------------

--
-- Table structure for table `api_picture`
--

CREATE TABLE `api_picture` (
  `id` int(11) NOT NULL,
  `picture` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_report`
--

CREATE TABLE `api_report` (
  `id` int(11) NOT NULL,
  `comment` varchar(256) NOT NULL,
  `created_by_id` int(11) NOT NULL,
  `reported_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `broadcast` tinyint(1) NOT NULL,
  `rated_created_by` tinyint(1) NOT NULL,
  `rated_working_with` tinyint(1) NOT NULL,
  `picture_required` tinyint(1) NOT NULL,
  `note` varchar(256) NOT NULL,
  `max_dist` double NOT NULL,
  `min_rating` double NOT NULL,
  `created_by_id` int(11) DEFAULT NULL,
  `destination_id` int(11) DEFAULT NULL,
  `direct_user_id` int(11) DEFAULT NULL,
  `working_with_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_request`
--

INSERT INTO `api_request` (`id`, `name`, `status`, `location_status`, `time`, `broadcast`, `rated_created_by`, `rated_working_with`, `picture_required`, `note`, `max_dist`, `min_rating`, `created_by_id`, `destination_id`, `direct_user_id`, `working_with_id`) VALUES
(1, 'Req1', 1, 0, '2020-05-21 18:00:00.000000', 1, 0, 0, 0, 'Budi brz', 10, 3.8, 4, 7, 2, 5),
(2, 'Req2', 0, 0, '2020-05-15 18:00:00.000000', 1, 0, 0, 0, 'Budi brz', 10, 3.8, 4, 9, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `api_requestedit`
--

CREATE TABLE `api_requestedit` (
  `id` int(11) NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  `request_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_requestedit`
--

INSERT INTO `api_requestedit` (`id`, `time`, `request_id`) VALUES
(1, '2020-05-21 18:00:00.000000', 1),
(2, '2020-05-21 18:00:00.000000', 1),
(3, '2020-05-21 18:00:00.000000', 1),
(4, '2020-05-21 18:00:00.000000', 1),
(5, '2020-05-21 18:00:00.000000', 1),
(6, '2020-05-21 18:00:00.000000', 1),
(7, '2020-05-21 18:00:00.000000', 1),
(8, '2020-05-21 18:00:00.000000', 1),
(9, '2020-05-21 18:00:00.000000', 1),
(10, '2020-05-21 18:00:00.000000', 1),
(11, '2020-05-21 18:00:00.000000', 1),
(12, '2020-05-21 18:00:00.000000', 1),
(13, '2020-05-21 18:00:00.000000', 1),
(14, '2020-05-21 18:00:00.000000', 1),
(15, '2020-05-21 18:00:00.000000', 1),
(16, '2020-05-21 18:00:00.000000', 1),
(17, '2020-05-21 18:00:00.000000', 1),
(18, '2020-05-21 18:00:00.000000', 1),
(19, '2020-05-21 18:00:00.000000', 1),
(20, '2020-05-21 18:00:00.000000', 1),
(21, '2020-05-21 18:00:00.000000', 1),
(22, '2020-05-21 18:00:00.000000', 1),
(23, '2020-05-21 18:00:00.000000', 1),
(24, '2020-05-21 18:00:00.000000', 1);

-- --------------------------------------------------------

--
-- Table structure for table `api_requestedit_tasks`
--

CREATE TABLE `api_requestedit_tasks` (
  `id` int(11) NOT NULL,
  `requestedit_id` int(11) NOT NULL,
  `taskedit_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_requestedit_tasks`
--

INSERT INTO `api_requestedit_tasks` (`id`, `requestedit_id`, `taskedit_id`) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5),
(6, 6, 6),
(7, 7, 7),
(8, 8, 8),
(9, 9, 9),
(10, 10, 10),
(11, 11, 11),
(12, 12, 12),
(13, 13, 13),
(14, 14, 14),
(15, 15, 15),
(16, 16, 16),
(17, 17, 17),
(18, 18, 18),
(19, 19, 19),
(20, 20, 20),
(21, 21, 21),
(22, 22, 22),
(23, 23, 23),
(24, 24, 24);

-- --------------------------------------------------------

--
-- Table structure for table `api_request_pictures`
--

CREATE TABLE `api_request_pictures` (
  `id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `picture_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_request_tasklist`
--

CREATE TABLE `api_request_tasklist` (
  `id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_request_tasklist`
--

INSERT INTO `api_request_tasklist` (`id`, `request_id`, `task_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3),
(4, 2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `api_service`
--

CREATE TABLE `api_service` (
  `id` int(11) NOT NULL,
  `service_type_sr` varchar(50) NOT NULL,
  `service_type_en` varchar(50) NOT NULL,
  `description_sr` varchar(256) NOT NULL,
  `description_en` varchar(256) NOT NULL,
  `picture_required` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_service`
--

INSERT INTO `api_service` (`id`, `service_type_sr`, `service_type_en`, `description_sr`, `description_en`, `picture_required`) VALUES
(1, 'Ostalo', 'Other', 'Ostalo...', 'Other...', 0),
(2, 'Kupovina', 'Shopping', 'Dostava paketa na kucnu adresu', 'Delivery at home address', 1),
(3, 'Placanje', 'Payment', 'Placanje racuna u banci', 'Paying bills at bank', 0),
(4, 'Setanje ljubimaca', 'Taking pets for a walk', 'Setanje po parku', 'Taking pets for a walk in a park', 0);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_task`
--

INSERT INTO `api_task` (`id`, `name`, `description`, `picture_required`, `address_id`, `service_type_id`) VALUES
(1, 'task1', 'Opis prvog taska.', 0, 8, 2),
(2, 'task2', 'Ovde nesto pise.', 0, 33, 2),
(3, 'task1', 'Kupi na zadatoj lokaciji.', 0, 10, 2),
(4, 'task2', 'Prosetaj mi racune', 0, NULL, 3);

-- --------------------------------------------------------

--
-- Table structure for table `api_taskedit`
--

CREATE TABLE `api_taskedit` (
  `id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_taskedit`
--

INSERT INTO `api_taskedit` (`id`, `address_id`, `task_id`) VALUES
(1, 11, 1),
(2, 12, 2),
(3, 13, 2),
(4, 14, 2),
(5, 15, 2),
(6, 16, 2),
(7, 17, 2),
(8, 18, 2),
(9, 19, 2),
(10, 20, 2),
(11, 21, 2),
(12, 22, 2),
(13, 23, 2),
(14, 24, 2),
(15, 25, 2),
(16, 26, 2),
(17, 27, 2),
(18, 28, 2),
(19, 29, 2),
(20, 30, 2),
(21, 31, 2),
(22, 32, 2),
(23, 33, 2),
(24, 34, 2);

-- --------------------------------------------------------

--
-- Table structure for table `api_task_checklist`
--

CREATE TABLE `api_task_checklist` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `checklist_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_task_checklist`
--

INSERT INTO `api_task_checklist` (`id`, `task_id`, `checklist_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3),
(4, 2, 4),
(5, 3, 5),
(6, 3, 6),
(7, 4, 7),
(8, 4, 8);

-- --------------------------------------------------------

--
-- Table structure for table `api_task_pictures`
--

CREATE TABLE `api_task_pictures` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `picture_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `is_admin` tinyint(1) NOT NULL,
  `email` varchar(254) NOT NULL,
  `phone` varchar(16) NOT NULL,
  `picture` varchar(100) DEFAULT NULL,
  `avg_rating` double DEFAULT NULL,
  `min_rating` double DEFAULT NULL,
  `max_dist` double DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `benefit_discount` double DEFAULT NULL,
  `benefit_requirement` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_user`
--

INSERT INTO `api_user` (`id`, `password`, `last_login`, `is_superuser`, `username`, `first_name`, `last_name`, `is_staff`, `is_active`, `date_joined`, `is_admin`, `email`, `phone`, `picture`, `avg_rating`, `min_rating`, `max_dist`, `status`, `benefit_discount`, `benefit_requirement`, `location_id`) VALUES
(1, 'pbkdf2_sha256$180000$5XcOMs3mVDVR$8bj2G74jdGSkEkIuOe/udPhjpXV3f/XzS0r2+z/v+jM=', NULL, 0, 'admin1@errand.com', 'Admin1', 'Admin1', 0, 1, '2020-05-14 15:51:49.421315', 1, 'admin1@errand.com', '', '', NULL, 1, 1000, 0, 0.1, 5, NULL),
(2, 'pbkdf2_sha256$180000$j0K5sSciKkmR$0TGiBwFF6EZR9yC2tuybu4bqIkyloNrn6yyWl9DUKQM=', NULL, 0, 'djordjepav@errand.com', 'Djordje', 'Pavlovic', 0, 1, '2020-05-14 15:56:46.663592', 0, 'djordjepav@errand.com', '+381123456789', 'db/images/users/2.png', NULL, 1, 1000, 0, 0.1, 5, NULL),
(3, 'pbkdf2_sha256$180000$KK8xU3Bp9S3F$KZiBf+gAEHADrFI+Aw7CK8QANxMwZwnw3NkAy0oY7Ro=', NULL, 0, 'masa@errand.com', 'Masa', 'Nesic', 0, 1, '2020-05-14 15:57:37.601717', 0, 'masa@errand.com', '+381123456789', 'db/images/users/3.png', NULL, 1, 1000, 0, 0.2, 10, NULL),
(4, 'pbkdf2_sha256$180000$9ugfPn4Bpvcd$Wu2CfSg+T4KfspiYrjVER/txSDv+e8ViiLSCg4PvpRI=', NULL, 0, 'uros@errand.com', 'Uros', 'Milivojevic', 0, 1, '2020-05-14 15:58:50.886538', 0, 'uros@errand.com', '+381123456789', 'db/images/users/4.png', NULL, 1, 1000, 0, 0.15, 7, NULL),
(5, 'pbkdf2_sha256$180000$sq3H8HFP2aYv$yPdcXDgPhAyO1akVp/tOOlhC9KDYHkleyQwj9in0so4=', NULL, 0, 'petar@errand.com', 'Petar', 'Trifunovic', 0, 1, '2020-05-14 16:00:17.301667', 0, 'petar@errand.com', '+381122466689', 'db/images/users/5.png', NULL, 3, 500, 0, 0.2, 10, NULL),
(6, 'pbkdf2_sha256$180000$NzaPqgBtViVi$7nXl+XJN+1ztamRm94AFrN6pgbnWOAt+JOYx2R3C/k8=', NULL, 0, 'admin2@errand.com', 'Admin2', 'Admin2', 0, 1, '2020-05-14 16:01:53.981249', 1, 'admin2@errand.com', '', 'db/images/users/6.png', NULL, 1, 1000, 0, 0.1, 5, NULL),
(7, 'pbkdf2_sha256$180000$YqJf3YgBmy3y$04byByRHevEGmi+rX/OV2fdRTUBxZw8P2w5DpGxHkn0=', NULL, 0, 'guest@errand.com', 'Guest', 'Guest', 0, 1, '2020-05-14 17:56:56.828166', 0, 'guest@errand.com', '+381123456789', '', NULL, 1, 1000, 0, 0.1, 5, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `api_userservice`
--

CREATE TABLE `api_userservice` (
  `id` int(11) NOT NULL,
  `max_dist` double NOT NULL,
  `payment_type` int(11) NOT NULL,
  `payment_ammount` double NOT NULL,
  `min_rating` double NOT NULL,
  `service_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_userservice`
--

INSERT INTO `api_userservice` (`id`, `max_dist`, `payment_type`, `payment_ammount`, `min_rating`, `service_id`) VALUES
(1, 10, 2, 600, 4.2, 1),
(2, 10, 2, 600, 4.2, 2),
(3, 15, 1, 600, 4, 2),
(4, 15, 1, 400, 4, 3),
(6, 15, 1, 400, 3.8, 4);

-- --------------------------------------------------------

--
-- Table structure for table `api_user_groups`
--

CREATE TABLE `api_user_groups` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_user_user_permissions`
--

CREATE TABLE `api_user_user_permissions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `api_workinghour`
--

CREATE TABLE `api_workinghour` (
  `id` int(11) NOT NULL,
  `work_from` time(6) NOT NULL,
  `work_until` time(6) NOT NULL,
  `day` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_workinghour`
--

INSERT INTO `api_workinghour` (`id`, `work_from`, `work_until`, `day`) VALUES
(1, '08:00:00.000000', '19:45:00.000000', 1),
(2, '08:00:00.000000', '19:45:00.000000', 1),
(3, '10:00:00.000000', '18:45:00.000000', 2),
(4, '10:00:00.000000', '18:45:00.000000', 3),
(5, '10:00:00.000000', '18:45:00.000000', 4),
(6, '10:00:00.000000', '18:30:00.000000', 4),
(7, '10:00:00.000000', '18:30:00.000000', 5),
(8, '10:00:00.000000', '18:30:00.000000', 3),
(9, '10:00:00.000000', '18:30:00.000000', 2),
(10, '09:00:00.000000', '18:30:00.000000', 0),
(11, '09:00:00.000000', '18:30:00.000000', 0),
(12, '09:00:00.000000', '18:30:00.000000', 2),
(13, '09:00:00.000000', '18:30:00.000000', 4),
(14, '11:00:00.000000', '18:30:00.000000', 4),
(15, '11:00:00.000000', '18:30:00.000000', 3),
(17, '11:00:00.000000', '18:30:00.000000', 1);

-- --------------------------------------------------------

--
-- Table structure for table `authtoken_token`
--

CREATE TABLE `authtoken_token` (
  `key` varchar(40) NOT NULL,
  `created` datetime(6) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `authtoken_token`
--

INSERT INTO `authtoken_token` (`key`, `created`, `user_id`) VALUES
('65266b0ad9440dd6d63922afb666f1495b0638f5', '2020-05-14 17:56:56.991728', 7),
('798be380e2f74de36d040cf16a1603b60d7cdcfb', '2020-05-14 16:18:12.568000', 2),
('cfd6e5d73c5ae3403b2769b4287cc993e6569165', '2020-05-14 16:01:54.876346', 6);

-- --------------------------------------------------------

--
-- Table structure for table `auth_group`
--

CREATE TABLE `auth_group` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `auth_group_permissions`
--

CREATE TABLE `auth_group_permissions` (
  `id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `auth_permission`
--

CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
(45, 'Can add location', 12, 'add_location'),
(46, 'Can change location', 12, 'change_location'),
(47, 'Can delete location', 12, 'delete_location'),
(48, 'Can view location', 12, 'view_location'),
(49, 'Can add notification', 13, 'add_notification'),
(50, 'Can change notification', 13, 'change_notification'),
(51, 'Can delete notification', 13, 'delete_notification'),
(52, 'Can view notification', 13, 'view_notification'),
(53, 'Can add picture', 14, 'add_picture'),
(54, 'Can change picture', 14, 'change_picture'),
(55, 'Can delete picture', 14, 'delete_picture'),
(56, 'Can view picture', 14, 'view_picture'),
(57, 'Can add request', 15, 'add_request'),
(58, 'Can change request', 15, 'change_request'),
(59, 'Can delete request', 15, 'delete_request'),
(60, 'Can view request', 15, 'view_request'),
(61, 'Can add service', 16, 'add_service'),
(62, 'Can change service', 16, 'change_service'),
(63, 'Can delete service', 16, 'delete_service'),
(64, 'Can view service', 16, 'view_service'),
(65, 'Can add task', 17, 'add_task'),
(66, 'Can change task', 17, 'change_task'),
(67, 'Can delete task', 17, 'delete_task'),
(68, 'Can view task', 17, 'view_task'),
(69, 'Can add working hour', 18, 'add_workinghour'),
(70, 'Can change working hour', 18, 'change_workinghour'),
(71, 'Can delete working hour', 18, 'delete_workinghour'),
(72, 'Can view working hour', 18, 'view_workinghour'),
(73, 'Can add user service', 19, 'add_userservice'),
(74, 'Can change user service', 19, 'change_userservice'),
(75, 'Can delete user service', 19, 'delete_userservice'),
(76, 'Can view user service', 19, 'view_userservice'),
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
(97, 'Can add full user', 25, 'add_fulluser'),
(98, 'Can change full user', 25, 'change_fulluser'),
(99, 'Can delete full user', 25, 'delete_fulluser'),
(100, 'Can view full user', 25, 'view_fulluser'),
(101, 'Can add full request', 26, 'add_fullrequest'),
(102, 'Can change full request', 26, 'change_fullrequest'),
(103, 'Can delete full request', 26, 'delete_fullrequest'),
(104, 'Can view full request', 26, 'view_fullrequest'),
(105, 'Can add banned', 27, 'add_banned'),
(106, 'Can change banned', 27, 'change_banned'),
(107, 'Can delete banned', 27, 'delete_banned'),
(108, 'Can view banned', 27, 'view_banned'),
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `django_content_type`
--

CREATE TABLE `django_content_type` (
  `id` int(11) NOT NULL,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `django_content_type`
--

INSERT INTO `django_content_type` (`id`, `app_label`, `model`) VALUES
(1, 'admin', 'logentry'),
(7, 'api', 'achievement'),
(8, 'api', 'achievementlevel'),
(9, 'api', 'address'),
(27, 'api', 'banned'),
(10, 'api', 'benefit'),
(11, 'api', 'checklist'),
(26, 'api', 'fullrequest'),
(25, 'api', 'fulluser'),
(12, 'api', 'location'),
(13, 'api', 'notification'),
(24, 'api', 'offer'),
(14, 'api', 'picture'),
(23, 'api', 'rating'),
(22, 'api', 'report'),
(15, 'api', 'request'),
(21, 'api', 'requestedit'),
(16, 'api', 'service'),
(17, 'api', 'task'),
(20, 'api', 'taskedit'),
(6, 'api', 'user'),
(19, 'api', 'userservice'),
(18, 'api', 'workinghour'),
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `django_migrations`
--

INSERT INTO `django_migrations` (`id`, `app`, `name`, `applied`) VALUES
(1, 'contenttypes', '0001_initial', '2020-05-14 15:22:05.271554'),
(2, 'contenttypes', '0002_remove_content_type_name', '2020-05-14 15:22:05.352414'),
(3, 'auth', '0001_initial', '2020-05-14 15:22:05.451108'),
(4, 'auth', '0002_alter_permission_name_max_length', '2020-05-14 15:22:05.975951'),
(5, 'auth', '0003_alter_user_email_max_length', '2020-05-14 15:22:05.981077'),
(6, 'auth', '0004_alter_user_username_opts', '2020-05-14 15:22:05.987379'),
(7, 'auth', '0005_alter_user_last_login_null', '2020-05-14 15:22:05.993786'),
(8, 'auth', '0006_require_contenttypes_0002', '2020-05-14 15:22:05.995904'),
(9, 'auth', '0007_alter_validators_add_error_messages', '2020-05-14 15:22:06.007810'),
(10, 'auth', '0008_alter_user_username_max_length', '2020-05-14 15:22:06.027121'),
(11, 'auth', '0009_alter_user_last_name_max_length', '2020-05-14 15:22:06.040839'),
(12, 'auth', '0010_alter_group_name_max_length', '2020-05-14 15:22:06.056471'),
(13, 'auth', '0011_update_proxy_permissions', '2020-05-14 15:22:06.069535'),
(14, 'api', '0001_initial', '2020-05-14 15:22:07.761443'),
(15, 'admin', '0001_initial', '2020-05-14 15:22:14.370088'),
(16, 'admin', '0002_logentry_remove_auto_add', '2020-05-14 15:22:14.639786'),
(17, 'admin', '0003_logentry_add_action_flag_choices', '2020-05-14 15:22:14.674889'),
(18, 'authtoken', '0001_initial', '2020-05-14 15:22:14.738001'),
(19, 'authtoken', '0002_auto_20160226_1747', '2020-05-14 15:22:15.006842'),
(20, 'sessions', '0001_initial', '2020-05-14 15:22:15.032634'),
(21, 'api', '0002_auto_20200514_1643', '2020-05-14 16:43:52.864734');

-- --------------------------------------------------------

--
-- Table structure for table `django_session`
--

CREATE TABLE `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  ADD KEY `api_fullrequest_rating_created_by_id_feb4b718_fk_api_rating_id` (`rating_created_by_id`),
  ADD KEY `api_fullrequest_rating_working_with_id_9c571685_fk_api_rating_id` (`rating_working_with_id`),
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
-- Indexes for table `api_location`
--
ALTER TABLE `api_location`
  ADD PRIMARY KEY (`id`);

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
  ADD KEY `api_request_working_with_id_f1b4ff98_fk_api_user_id` (`working_with_id`),
  ADD KEY `api_request_created_by_id_0c7d7077_fk_api_user_id` (`created_by_id`),
  ADD KEY `api_request_destination_id_930cc9e2_fk_api_address_id` (`destination_id`),
  ADD KEY `api_request_direct_user_id_e6182389_fk_api_user_id` (`direct_user_id`);

--
-- Indexes for table `api_requestedit`
--
ALTER TABLE `api_requestedit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_requestedit_request_id_83148a3e_fk_api_request_id` (`request_id`);

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
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `api_user_location_id_d4a7bef9_fk_api_location_id` (`location_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `api_achievementlevel`
--
ALTER TABLE `api_achievementlevel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_address`
--
ALTER TABLE `api_address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `api_banned`
--
ALTER TABLE `api_banned`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_benefit`
--
ALTER TABLE `api_benefit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `api_checklist`
--
ALTER TABLE `api_checklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `api_fullrequest`
--
ALTER TABLE `api_fullrequest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `api_fullrequest_offers`
--
ALTER TABLE `api_fullrequest_offers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `api_fulluser`
--
ALTER TABLE `api_fulluser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `api_fulluser_ratings`
--
ALTER TABLE `api_fulluser_ratings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_fulluser_requests`
--
ALTER TABLE `api_fulluser_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `api_fulluser_services`
--
ALTER TABLE `api_fulluser_services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `api_fulluser_working_hours`
--
ALTER TABLE `api_fulluser_working_hours`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `api_location`
--
ALTER TABLE `api_location`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_notification`
--
ALTER TABLE `api_notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_offer`
--
ALTER TABLE `api_offer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `api_requestedit`
--
ALTER TABLE `api_requestedit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `api_requestedit_tasks`
--
ALTER TABLE `api_requestedit_tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `api_request_pictures`
--
ALTER TABLE `api_request_pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_request_tasklist`
--
ALTER TABLE `api_request_tasklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `api_service`
--
ALTER TABLE `api_service`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `api_task`
--
ALTER TABLE `api_task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `api_taskedit`
--
ALTER TABLE `api_taskedit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `api_task_checklist`
--
ALTER TABLE `api_task_checklist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `api_task_pictures`
--
ALTER TABLE `api_task_pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `api_user`
--
ALTER TABLE `api_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `api_userservice`
--
ALTER TABLE `api_userservice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

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
  ADD CONSTRAINT `api_fullrequest_rating_created_by_id_feb4b718_fk_api_rating_id` FOREIGN KEY (`rating_created_by_id`) REFERENCES `api_rating` (`id`),
  ADD CONSTRAINT `api_fullrequest_rating_working_with_id_9c571685_fk_api_rating_id` FOREIGN KEY (`rating_working_with_id`) REFERENCES `api_rating` (`id`),
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
  ADD CONSTRAINT `api_request_destination_id_930cc9e2_fk_api_address_id` FOREIGN KEY (`destination_id`) REFERENCES `api_address` (`id`),
  ADD CONSTRAINT `api_request_direct_user_id_e6182389_fk_api_user_id` FOREIGN KEY (`direct_user_id`) REFERENCES `api_user` (`id`),
  ADD CONSTRAINT `api_request_working_with_id_f1b4ff98_fk_api_user_id` FOREIGN KEY (`working_with_id`) REFERENCES `api_user` (`id`);

--
-- Constraints for table `api_requestedit`
--
ALTER TABLE `api_requestedit`
  ADD CONSTRAINT `api_requestedit_request_id_83148a3e_fk_api_request_id` FOREIGN KEY (`request_id`) REFERENCES `api_request` (`id`);

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
-- Constraints for table `api_user`
--
ALTER TABLE `api_user`
  ADD CONSTRAINT `api_user_location_id_d4a7bef9_fk_api_location_id` FOREIGN KEY (`location_id`) REFERENCES `api_location` (`id`);

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
