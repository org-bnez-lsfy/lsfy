package org.bnez.xiaoyue.lsfy.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class ReadFileLines
{
	public static int count(String filename) throws IOException
	{
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		byte[] c = new byte[1024];
		int count = 0;
		int readChars = 0;
		while ((readChars = is.read(c)) != -1)
		{
			for (int i = 0; i < readChars; ++i)
			{
				if (c[i] == '\n')
					++count;
			}
		}
		is.close();
		return count;
	}
	
	public static void main(String[] args) throws IOException
	{
		int zero = 0;
		int files = 0;
		long total = 0;
		String[] ss = {"txt"};
		@SuppressWarnings("unchecked")
		Collection<File> fs = FileUtils.listFiles(new File("c:/comb"), ss, false);
		for(File f : fs)
		{
			int c = count(f.getAbsolutePath());
			if(c == 0)
				zero ++;
			files++;
			total += c;
			System.out.println(f.getAbsolutePath() + "\t" + c);
		}
		System.out.println("files " + files + ", total " + total + ", zero " + zero + ", zero/total " + (float)zero/files);
	}
}
