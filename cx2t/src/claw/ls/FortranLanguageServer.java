package claw.ls;

import java.io.StringReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import claw.tatsu.xcodeml.xnode.common.Xcode;
import claw.tatsu.xcodeml.xnode.common.XcodeProgram;
import claw.tatsu.xcodeml.xnode.common.Xnode;
import claw.tatsu.xcodeml.xnode.fortran.FfunctionDefinition;
import claw.tatsu.xcodeml.xnode.fortran.FfunctionType;

public class FortranLanguageServer implements LanguageServer, TextDocumentService, WorkspaceService {

	HashMap<String, TextDocumentItem> documents = new HashMap<>();
	String workspaceRoot;
	HashMap<String, XcodeProgram> translationUnits = new HashMap<>();
	
	@Override
	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
//		ServerCapabilities capabilities = new ServerCapabilities();
//		capabilities.setTypeDefinitionProvider(params.getCapabilities().getTextDocument().getTypeDefinition().getDynamicRegistration());
//		InitializeResult result = new InitializeResult();
//		result.setCapabilities(capabilities);
//		return CompletableFuture.completedFuture(result);
		
        workspaceRoot = params.getRootUri();

        ServerCapabilities capabilities = new ServerCapabilities();
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        capabilities.setCodeActionProvider(false);
        List<String> triggerChars = new ArrayList<String>();
        triggerChars.add(" ");
        capabilities.setCompletionProvider(new CompletionOptions(true, triggerChars));

        return CompletableFuture.completedFuture(new InitializeResult(capabilities));	
	}

	@Override
	public CompletableFuture<Object> shutdown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextDocumentService getTextDocumentService() {
		return this;
	}

	@Override
	public WorkspaceService getWorkspaceService() {
		return this;
	}

	@Override
	public void didChangeConfiguration(DidChangeConfigurationParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        documents.put(params.getTextDocument().getUri(), params.getTextDocument());
        String xmluri = params.getTextDocument().getUri()+".xml";
        xmluri = xmluri.replaceAll("file://", "");
        XcodeProgram prog = XcodeProgram.createFromFile(xmluri);
        if(prog == null) {
        	System.err.println("not good");
        } else {
        	System.err.println("Opening " + xmluri);
        	System.err.println("opened " + prog);
        }
        translationUnits.put(params.getTextDocument().getUri(), prog);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        for (TextDocumentContentChangeEvent changeEvent : params.getContentChanges()) {
            // Will be full update because we specified that is all we support
            if (changeEvent.getRange() != null) {
                throw new UnsupportedOperationException("Range should be null for full document update.");
            }
            if (changeEvent.getRangeLength() != null) {
                throw new UnsupportedOperationException("RangeLength should be null for full document update.");
            }

            documents.get(uri).setText(changeEvent.getText());
//            Document doc = convertStringToXMLDocument( changeEvent.getText() );
//            translationUnits.replace(uri, XcodeProgram.createFromDocument(doc));
        }
    }
    
//    private static Document convertStringToXMLDocument(String xmlString)
//    {
//        //Parser that produces DOM object trees from XML content
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//         
//        //API to obtain DOM Document instance
//        DocumentBuilder builder = null;
//        try
//        {
//            //Create DocumentBuilder with default configuration
//            builder = factory.newDocumentBuilder();
//             
//            //Parse the content to Document object
//            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
//            return doc;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        documents.remove(uri);
        translationUnits.remove(uri);
}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		// TODO Auto-generated method stub
	}
	
	public static String getLine(TextDocumentItem textDocumentItem, Position position) {
		String text = textDocumentItem.getText();
		String[] lines = text.split("\\r?\\n", position.getLine() + 2);
		if (lines.length >= position.getLine() + 1) {
			return lines[position.getLine()];
		}
		return null;
	}
	
	 @Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
		 XcodeProgram xcodeml = translationUnits.get(position.getTextDocument().getUri());
		 System.err.println("Entered completion(), checking " + xcodeml);
		 TextDocumentItem doc = documents.get(position.getTextDocument().getUri());
		 String line = getLine(doc, position.getPosition());
		 List<CompletionItem> completions = new ArrayList<CompletionItem>();
		 try {
			 String substr = line.substring(position.getPosition().getCharacter()-5, position.getPosition().getCharacter());
			 if(substr.equalsIgnoreCase("call ")) {
				 System.err.println("Found " + xcodeml.matchAll(Xcode.F_FUNCTION_DEFINITION).size() + " functions");
				 for(Xnode fctDef : xcodeml.matchAll(Xcode.F_FUNCTION_DEFINITION)) {
					 System.err.println(fctDef.matchDescendant(Xcode.NAME).value());
					 CompletionItem fctDefItem = new CompletionItem();
					 fctDefItem.setLabel(fctDef.matchDescendant(Xcode.NAME).value());
					 fctDefItem.setKind(CompletionItemKind.Function);
					 fctDefItem.setSortText("0");
					 String text = fctDef.matchDescendant(Xcode.NAME).value() + "(";
					 
					 FfunctionType fctType = xcodeml.getTypeTable().getFunctionType(fctDef.getType());
					 List<String> params = fctType.getParamsNames();
					 boolean needComma = false;
					 for(String param : params) {
					     if(needComma)
					    	 text += ", ";
					      needComma = true;
					      text += param;
				     }
					 text += ")";
					 fctDefItem.setInsertText(text);
//					 if(anc != null ) {
//						 for(Xnode sib : anc.prevSibling())
//						 args.prevSibling() != null
//							        && Xnode.isOfCode(args.prevSibling(), Xcode.NAME)
//							        && args.prevSibling().value().equalsIgnoreCase(name.toString()))
//					 }
					 completions.add(fctDefItem);
				 }
			 }
		 }
		 catch( java.lang.StringIndexOutOfBoundsException e) {
			 
		 }
         return CompletableFuture.completedFuture(Either.<List<CompletionItem>, CompletionList>forRight( new CompletionList(false, completions)));
	}

//     @Override
     public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem item) {
//         if (item.getData().equals(1.0)) {
//             item.setDetail("TypeScript details");
//             item.setDocumentation("TypeScript documentation");
//         } else if (item.getData().equals(2.0)) {
//             item.setDetail("JavaScript details");
//             item.setDocumentation("JavaScript documentation");
//         }
         return CompletableFuture.completedFuture(item);
     }

}
