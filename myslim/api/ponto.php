<?php
    /*GET ALL*/
	$app->GET('/api/pontoAll', function($request)
	{
		require_once('db/dbconnect.php');
		foreach($db->ponto()
				as $row) {
			$data[] = $row;
		}
		if(isset($data))
		{
			echo json_encode($data, JSON_UNESCAPED_UNICODE);
		}
		else
		{
			echo json_encode("Dados inexistentes.", JSON_UNESCAPED_UNICODE);
		}	
	});
	
	/*ADD*/
	$app->POST('/api/addPonto', function($request)
	{
		require_once('db/dbconnect.php');
		//$task = json_decode($request->getBody(), true);
		$titulo = $_POST["titulo"];
		$lati = $_POST["lati"];
		$longi = $_POST["longi"];
		$tipo_id = $_POST["tipo_id"];
		$utilizador_id = $_POST["utilizador_id"];
		$data = array
		(
			"titulo" => $titulo,
			"lati" => $lati,
			"longi" => $longi,
			"tipo_id" => $tipo_id,
			"utilizador_id" => $utilizador_id
		);
		$ponto = $db->ponto();
		$result = $ponto->insert($data);
		if($result == false)
		{
			$result = ['status' => false, 'MSG' => "Inserção falhou"];
			echo json_encode($result, JSON_UNESCAPED_UNICODE);
		}
		else
		{
			$result = ['status' => true, 'MSG' => "Registo inserido tem o id ".$result["id"]];
			echo json_encode($result, JSON_UNESCAPED_UNICODE);
		}
	});
	
	/*GET FILTER*/
	$app->GET('/api/pontoFilter/{tipo}', function($request)
	{
	    $tipo = $request->getAttribute('tipo');
		require_once('db/dbconnect.php');
		foreach($db->ponto()
		        ->where('tipo_id', $tipo)
				as $row) {
			$data[] = $row;
		}
		if(isset($data))
		{
			echo json_encode($data, JSON_UNESCAPED_UNICODE);
		}
		else
		{
			echo json_encode("Dados inexistentes.", JSON_UNESCAPED_UNICODE);
		}	
	});
?>