import * as path from 'path';
import { workspace, ExtensionContext } from 'vscode';

import {
	LanguageClient,
	LanguageClientOptions,
	ServerOptions,
	TransportKind
} from 'vscode-languageclient';

let client: LanguageClient;

export function activate(context: ExtensionContext) {
	// let executablePath = "java -cp /home/vogtha/git/claw-compiler/clawls-export/clawls.jar:/home/vogtha/git/claw-compiler/clawls-export/gson-2.8.5.jar claw.ls.FortranLanguageServerLauncher";
	//  context.asAbsolutePath(
	// 	path.join('..', '..', '..', 'install', 'bin', 'sample_server' )
	// );
	let serverOptions: ServerOptions = {
		command: '/home/vogtha/jdk-12/bin/java',
		args: [ "-cp", "/home/vogtha/git/claw-compiler/clawls-export/clawls.jar:/home/vogtha/git/claw-compiler/clawls-export/gson-2.8.5.jar", "claw.ls.FortranLanguageServerLauncher"]
    };

	// Options to control the language client
	let clientOptions: LanguageClientOptions = {
		documentSelector: [
			{scheme: 'file', language: 'fortran'},
			{scheme: 'file', language: 'fortran_fixed-form'},
			{scheme: 'file', language: 'FortranFreeForm'}
		],
		synchronize: {
			// Notify the server about file changes to fortran files contain in the workspace
			fileEvents: workspace.createFileSystemWatcher('**/*.{f,F,f77,F77,f90,F90,f95,F95,f03,F03,f08,F08,for,FOR,fpp,FPP}')
		},
		// // Register the server for plain text documents
		// documentSelector: [{ scheme: 'file', language: 'fortran' }],
		// synchronize: {
		// 	// Notify the server about file changes to '.clientrc files contained in the workspace
		// 	fileEvents: workspace.createFileSystemWatcher('**/.clientrc')
		// },
		outputChannelName: 'clawls'
	};

	// Create the language client and start the client.
	client = new LanguageClient(
		'clawls',
		'Claw Fortran Language Server',
		serverOptions,
		clientOptions
	);

	// Start the client. This will also launch the server
	client.start();
}

export function deactivate(): Thenable<void> | undefined {
	if (!client) {
		return undefined;
	}
	return client.stop();
}
