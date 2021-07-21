<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require 'vendor/autoload.php';

$app = new \Slim\App();

require_once('api/utilizador.php');
require_once('api/ponto.php');
require_once('api/tipo.php');

$app->run();