<?php
	/*GET ALL*/
	$app->GET('/api/tipoAll', function($request)
	{
		require_once('db/dbconnect.php');
		foreach($db->tipo()
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