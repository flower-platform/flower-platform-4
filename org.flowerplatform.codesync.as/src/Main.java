import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import macromedia.asc.parser.Parser;
import macromedia.asc.parser.ProgramNode;
import macromedia.asc.util.Context;
import macromedia.asc.util.ContextStatics;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Context cx = new Context(new ContextStatics());
		InputStream in = new FileInputStream("test/CorePlugin.as");
		String origin = "";
		Parser parser = new Parser(cx, in, origin);
		ProgramNode program = parser.parseProgram();
		System.out.println(program);
	}

}
