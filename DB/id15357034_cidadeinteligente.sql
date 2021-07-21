-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 20, 2021 at 08:05 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id15357034_cidadeinteligente`
--

-- --------------------------------------------------------

--
-- Table structure for table `ponto`
--

CREATE TABLE `ponto` (
  `id` int(11) NOT NULL,
  `titulo` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `lati` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `longi` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `tipo_id` int(11) NOT NULL,
  `utilizador_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `ponto`
--

INSERT INTO `ponto` (`id`, `titulo`, `lati`, `longi`, `tipo_id`, `utilizador_id`) VALUES
(1, 'teste1', '41.693117', '-8.844428', 1, 1),
(2, 'teste2', '41.693726', '-8.833914', 1, 1),
(3, 'teste3', '41.691418', '-8.829279', 2, 1),
(4, 'teste4', '41.698180', '-8.826446', 2, 2),
(5, 'teste5', '41.701448', '-8.829708', 3, 2),
(6, 'teste6', '41.698949', '-8.845630', 1, 2),
(11, 'gddhhf', '41.697792', '8.8429176', 1, 1),
(12, 'teste35346', '8', '48', 1, 1),
(13, 'bgfi', '41.69779', '-8.842917', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tipo`
--

CREATE TABLE `tipo` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tipo`
--

INSERT INTO `tipo` (`id`, `nome`) VALUES
(1, 'Acidente'),
(2, 'Obras'),
(3, 'Etc');

-- --------------------------------------------------------

--
-- Table structure for table `utilizador`
--

CREATE TABLE `utilizador` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `utilizador`
--

INSERT INTO `utilizador` (`id`, `nome`, `password`) VALUES
(1, 'Diogo', '123'),
(2, 'Gonçalo', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ponto`
--
ALTER TABLE `ponto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tipo_id` (`tipo_id`) USING BTREE,
  ADD KEY `utilizador_id` (`utilizador_id`) USING BTREE;

--
-- Indexes for table `tipo`
--
ALTER TABLE `tipo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `utilizador`
--
ALTER TABLE `utilizador`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ponto`
--
ALTER TABLE `ponto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tipo`
--
ALTER TABLE `tipo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `utilizador`
--
ALTER TABLE `utilizador`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ponto`
--
ALTER TABLE `ponto`
  ADD CONSTRAINT `id_tipo` FOREIGN KEY (`tipo_id`) REFERENCES `tipo` (`id`),
  ADD CONSTRAINT `id_utilizador` FOREIGN KEY (`utilizador_id`) REFERENCES `utilizador` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
