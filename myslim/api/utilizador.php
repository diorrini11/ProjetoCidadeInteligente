<?php
	/*GET WHERE MULTIPLO*/
	$app->GET('/api/utilizadorWhereNI/{nome}/{password}', function($request)
	{
		$nome = $request->getAttribute('nome');
		$password = $request->getAttribute('password');
		require_once('db/dbconnect.php');
		foreach($db->utilizador()
				->where('nome LIKE ?', '%'.$nome.'%')
				->where('password LIKE ?', '%'.$password.'%')
				as $row)
		{
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
	/*POST*/
	$app->POST('/api/utilizador', function($request)
	{
		require_once('db/dbconnect.php');
		//$task = json_decode($request->getBody(), true);
		$nome = $_POST["nome"];
		$password = $_POST["password"];
		$data = array
		(
			"nome" => $nome,
			"password" => $password
		);
		$utilizador = $db->utilizador();
		$result = $utilizador->insert($data);
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
?>