package net.sc.pd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Pr {

	public static void main(String[] args) throws Exception {
		
		FileInputStream ins = new FileInputStream(new File("C:\\Users\\magic\\git\\shch\\ShCh\\src\\main\\java\\resources\\jhi8216317066826199192ij"));
		
		StringBuilder sb = new StringBuilder();
		
		int l = 0;
		l = ex(ins);
		System.out.println(l);
		for (int i = 0; i < l; i++) {
			int s = ex(ins);
			System.out.println(i + " : " + s);
			byte[] bs = new byte[s];
			ins.read(bs );
//			System.out.println(i + " : " + new String(bs));
			final String str = new String(bs);
			sb.append(str.replaceAll("\n", "")).append("\n");
		}
		
		ins.close();
		
		System.out.println(sb.toString());
	}

	private static int ex(FileInputStream ins) throws IOException {
		return ((ins.read() & 0xFF) << 24) + ((ins.read() & 0xFF) << 16) + ((ins.read() & 0xFF) << 8) + (ins.read() & 0xFF);
	}
}
