var tool_usage = require(__base + 'app/logic/util/tool_usage');
var config = require(__base + 'config/config');
var userfile_utils = require(__base + 'app/logic/util/userfile_utils');
var child_process = require('child_process');
function kindAnalyisis(req,res){
	const sessionID = req.params.sessionID;
	const file = req.params.file;
	let pathToFile = userfile_utils.fileInWorkspace(sessionID, file);
	const command = 'java -jar ${config.MCLAB_CORE_JAR_PATH} --json ${pathToFile} ';
	child_process.exec(command, function(error, stdout) {
		 try {
			 let output = JSON.parse(stdout);
			 res.json(output);
		 } catch(err) {
		 	 res.status(400).json({msg: 'Mclab-core failed to do kindAnalyisis analysis on this file. Is this a valid matlab file?'})
		 }
	});
}
module.export = {
	kindAnalyisis
}
