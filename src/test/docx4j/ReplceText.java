package test.docx4j;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;

/*
 * docx4j를 이용하여 docx내의 문구를 특정문구로 수정하는 예제
 */
public class ReplceText {
	public static void main(String[] args) throws Exception {
		WordprocessingMLPackage mlp = WordprocessingMLPackage.load(new File("d:/test.docx"));
		replaceText(mlp.getMainDocumentPart(), "[image]" ,"테스트");
		mlp.save(new File("d:/2.docx"));
	}

	static void replaceText(ContentAccessor c, String target, String replace) throws Exception {
		for (Object p : c.getContent()) {
			if (p instanceof ContentAccessor) {
				replaceText((ContentAccessor) p, target, replace);
			} else if (p instanceof JAXBElement) {
				Object v = ((JAXBElement<?>) p).getValue();

				if (v instanceof ContentAccessor) {
					replaceText((ContentAccessor) v, target, replace);
				} else if (v instanceof org.docx4j.wml.Text) {
					org.docx4j.wml.Text t = (org.docx4j.wml.Text) v;
					String text = t.getValue();

					if (text != null) {
						// System.out.println(text);
						// t.setSpace("preserve"); // needed?
						// t.setValue(replaceParams(text));
						if (text.intern() == target.intern()) {
							// System.out.println(text);
							t.setValue(replace);
						}
					}
				}
			}
		}
	}

	static Pattern	paramPatern	= Pattern.compile("(?i)(\\$\\{([\\w\\.]+)\\})");

	static String replaceParams(String text) {
		Matcher m = paramPatern.matcher(text);

		if (!m.find())
			return text;

		StringBuffer sb = new StringBuffer();
		String param, replacement;

		do {
			param = m.group(2);

			if (param != null) {
				replacement = getParamValue(param);
				m.appendReplacement(sb, replacement);
			} else
				m.appendReplacement(sb, "");
		} while (m.find());

		m.appendTail(sb);
		return sb.toString();
	}

	static String getParamValue(String name) {
		// replace from map or something else
		return name;
	}
}
