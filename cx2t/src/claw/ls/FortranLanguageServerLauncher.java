package claw.ls;

import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import org.eclipse.lsp4j.jsonrpc.Launcher;

public class FortranLanguageServerLauncher {

	public static void main(String[] args) {
		FortranLanguageServer server = new FortranLanguageServer();
		Launcher<LanguageClient> launcher = 
			    LSPLauncher.createServerLauncher(server,
			                                     System.in, 
			                                     System.out);
		launcher.startListening();
	}

}
