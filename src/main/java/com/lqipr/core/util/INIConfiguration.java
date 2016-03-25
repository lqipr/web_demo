package com.lqipr.core.util;

import java.io.*;
import java.net.URL;
import java.util.*;

public class INIConfiguration {
	private static final String COMMENT_CHARS = "#;";
	private static final String SEPARATOR_CHARS = "=:";
	private static final String QUOTE_CHARACTERS = "\"'";
	private static final String LINE_CONT = "\\";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private final Map<String,Map<String,String>> props=new LinkedHashMap<String,Map<String,String>>();

	public INIConfiguration(){

	}

	private static boolean isCommentChar(char c)
	{
		return COMMENT_CHARS.indexOf(c) >= 0;
	}
	private boolean isCommentLine(String line){
		if (line == null) return false;
		// blank lines are also treated as comment lines
		return line.isEmpty()|| COMMENT_CHARS.indexOf(line.charAt(0)) >= 0;
	}
	private static int findSeparator(String line){
		int index =
				findSeparatorBeforeQuote(line,
						findFirstOccurrence(line, QUOTE_CHARACTERS));
		if (index < 0)
		{
			index = findFirstOccurrence(line, SEPARATOR_CHARS);
		}
		return index;
	}
	private static int findSeparatorBeforeQuote(String line, int quoteIndex){
		int index = quoteIndex - 1;
		while (index >= 0 && Character.isWhitespace(line.charAt(index))){
			index--;
		}
		if (index >= 0 && SEPARATOR_CHARS.indexOf(line.charAt(index)) < 0) index=-1;
		return index;
	}
	private static int findFirstOccurrence(String line, String separators){
		int index = -1;
		for (char sep:separators.toCharArray()){
			int pos = line.indexOf(sep);
			if (pos >= 0){
				if (index < 0 || pos < index){
					index=pos;
				}
			}
		}
		return index;
	}
	private boolean isSectionLine(String line){
		if (line == null) return false;
		return line.startsWith("[") && line.endsWith("]");
	}
	private static String parseValue(String val, BufferedReader reader) throws IOException{
		StringBuilder propertyValue = new StringBuilder();
		boolean lineContinues;
		String value = val.trim();
		do{
			boolean quoted = QUOTE_CHARACTERS.indexOf(value.charAt(0))>=0;
			boolean stop = false;
			boolean escape = false;
			char quote = quoted ? value.charAt(0) : 0;
			int i = quoted ? 1 : 0;
			StringBuilder result = new StringBuilder();
			char lastChar = 0;
			while (i < value.length() && !stop){
				char c = value.charAt(i);
				if (quoted){
					if ('\\' == c && !escape){
						escape = true;
					}else if (!escape && quote == c){
						stop = true;
					}else if (escape && quote == c){
						escape = false;
						result.append(c);
					}else{
						if (escape){
							escape = false;
							result.append('\\');
						}
						result.append(c);
					}
				}else{
				    if (isCommentChar(c) && Character.isWhitespace(lastChar)){
				    	stop = true;
					}else{
						result.append(c);
					}
				}
					
				i++;
				lastChar = c;
			}
			String v = result.toString();
			if (!quoted){
				v = v.trim();
				lineContinues = lineContinues(v);
				if (lineContinues){
				    // remove trailing "\"
					v = v.substring(0, v.length() - 1).trim();
				}
			}else{
				lineContinues = lineContinues(value, i);
			}
			propertyValue.append(v);
			if (lineContinues){
				propertyValue.append(LINE_SEPARATOR);
				value = reader.readLine();
			}
		}while(lineContinues && value != null);
		return propertyValue.toString();
	}
	private static boolean lineContinues(String line){
		String s = line.trim();
		return s.equals(LINE_CONT)
				|| (s.length() > 2 && s.endsWith(LINE_CONT) && Character
						.isWhitespace(s.charAt(s.length() - 2)));
	}
	private static boolean lineContinues(String line, int pos)
	{
		String s;
		if (pos >= line.length()){
			s = line;
		}else{
			int end = pos;
			while (end < line.length() && !isCommentChar(line.charAt(end)))
			{
				end++;
			}
			s = line.substring(pos, end);
		}
		return lineContinues(s);
	}
	public void read(URL url)throws IOException{
		read(url.openStream());
	}
	public void read(String file)throws IOException{
		read(new FileReader(file));
	}
	public void read(File file)throws IOException{
		read(new FileReader(file));
	}
	public void read(InputStream is)throws IOException{
		read(new InputStreamReader(is));
	}
	public void read(Reader in)throws IOException{
		BufferedReader bufferedReader = new BufferedReader(in);
		String line = bufferedReader.readLine();
		String section="";
		while (line != null){
			line = line.trim();
			if (!isCommentLine(line)){
				if (isSectionLine(line)){
					section = line.substring(1, line.length() - 1);
				}else{
					String key = "";
					String value = "";
					int index = findSeparator(line);
					if (index >= 0){
						key = line.substring(0, index);
						value = parseValue(line.substring(index + 1),bufferedReader);
					}else{
						key = line;
					}
					key = key.trim();
					if (key.length() < 1){
						// use space for properties with no key
						key = " ";
					}
					if(!props.containsKey(section)){
						Map<String,String> map=new HashMap<String,String>();
						props.put(section, map);
					}
					props.get(section).put(key, value);
				}
			}
			line = bufferedReader.readLine();

		}
	}
	public Set<String> getSections(){
		return Collections.unmodifiableSet(props.keySet());
	}
	public Map<String,Map<String,String>> getAllSection(){
		return Collections.unmodifiableMap(props);
	}
	public Map<String,String> getSection(String section){
		return  Collections.unmodifiableMap(props.get(section));
	}
	public boolean isEmpty(){
		return props.isEmpty();
	}
	public int size(){
		return props.size();
	}


}
