package cn.transfer.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.Enumeration;
import java.util.Vector;

public class TestIO {
	public static void main(String[] args) throws IOException {
		testPushbackInputStream();
	}

	static void testFile() {
		File f = new File("D:/files/create.txt");
		try {
			f.createNewFile(); // ���ҽ��������ھ��д˳���·����ָ�����Ƶ��ļ�ʱ�����ɷֵش���һ���µĿ��ļ���
			System.out.println("�÷�����С" + f.getTotalSpace()
					/ (1024 * 1024 * 1024) + "G"); // �����ɴ˳���·������ʾ���ļ���Ŀ¼�����ơ�
			f.mkdirs(); // �����˳���·����ָ����Ŀ¼���������б��赫�����ڵĸ�Ŀ¼��
			// f.delete(); // ɾ���˳���·������ʾ���ļ���Ŀ¼
			System.out.println("�ļ���  " + f.getName()); // �����ɴ˳���·������ʾ���ļ���Ŀ¼�����ơ�
			System.out.println("�ļ���Ŀ¼�ַ��� " + f.getParent());// ���ش˳���·������Ŀ¼��·�����ַ����������·����û��ָ����Ŀ¼���򷵻�
															// null��

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���Խ�streamReader.read()�ĳ�streamReader.read(byte[]b)�˷�����ȡ���ֽ���Ŀ�����ֽ�����ĳ��ȣ�
	 * ��ȡ�����ݱ��洢���ֽ������У� ���ض�ȡ���ֽ�����InputStream������������mark,reset,markSupported������ ���磺
	 * markSupported �жϸ���������֧��mark �� reset ������ mark���ڱ�ǵ�ǰλ�ã�
	 * �ڶ�ȡһ������������(С��readlimit������)��ʹ��reset���Իص�mark��ǵ�λ�á�
	 * FileInputStream��֧��mark/reset������BufferedInputStream֧�ִ˲�����
	 * mark(readlimit)�ĺ������ڵ�ǰλ����һ����ǣ��ƶ��������¶�ȡ������ֽ�����Ҳ����˵�������Ǻ��ȡ���ֽ�������readlimit��
	 * �����Ҳ�ز���������λ���ˡ� ͨ��InputStream��read()����-1��˵�������ļ�β�������ٶ�ȡ������ʹ����mark/reset��
	 */
	static void testFileInputStream() {
		int count = 0; // ͳ���ļ��ֽڳ���
		InputStream streamReader = null; // �ļ�������
		try {
			streamReader = new FileInputStream(new File(
					"D:/files/tiger.jpg"));
			/*
			 * 1.new File()������ļ���ַҲ����д��D:\\David\\Java\\java
			 * �߼�����\\files\\tiger.jpg,ǰһ��\�������Ժ�һ��
			 * ����ת���ģ�FileInputStream���л������ģ���������֮�����رգ�������ܵ����ڴ�ռ�������ݶ�ʧ��
			 */
			while (streamReader.read() != -1) { // ��ȡ�ļ��ֽڣ�������ָ�뵽��һ���ֽ�
				count++;
			}
			System.out.println("---�����ǣ� " + count + " �ֽ�");
		} catch (final IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} finally {
			try {
				streamReader.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}

	/**
	 * Java I/OĬ���ǲ��������ģ���ν�����塱�����ȰѴ����еõ���һ���ֽ������ݴ���һ������Ϊbuffer���ڲ��ֽ������
	 * Ȼ�������һ����ȡ����һ������ֽ����ݣ�û�л������ֻ��һ���ֽ�һ���ֽڶ���Ч��������һĿ��Ȼ��
	 * �����������������ʵ���˻��幦�ܣ�һ�������ǳ��õ�BufferedInputStream
	 */
	static void testFileInputStreamBuffer() {
		// TODO�Զ����ɵķ������
		byte[] buffer = new byte[512]; // һ��ȡ�����ֽ�����С,��������С
		int numberRead = 0;
		FileInputStream input = null;
		FileOutputStream out = null;
		try {
			input = new FileInputStream(
					"D:/files/tiger.jpg");
			out = new FileOutputStream(
					"D:/files/tiger2.jpg"); // ����ļ������ڻ��Զ�����

			while ((numberRead = input.read(buffer)) != -1) { // numberRead��Ŀ�����ڷ�ֹ���һ�ζ�ȡ���ֽ�С��buffer���ȣ�
				out.write(buffer, 0, numberRead); // ������Զ������0
			}
		} catch (final IOException e) {
		} finally {
			try {
				input.close();
				out.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * ��д����ObjectInputStream ��ObjectOutputStream �����������ȡ��д���û��Զ�����࣬����Ҫʵ�����ֹ��ܣ�
	 * ����ȡ��д��������ʵ��Serializable�ӿڣ���ʵ�ýӿڲ�û��ʲô�����������൱��һ����Ƕ��ѣ�����ȷʵ����ȱ�ٵ�
	 */
	static void testObjectInputStream() {
		ObjectOutputStream objectwriter = null;
		ObjectInputStream objectreader = null;
		try {
			objectwriter = new ObjectOutputStream(new FileOutputStream(
					"D:/files/student.txt"));
			objectwriter.writeObject(new Student("gg", 22));
			objectwriter.writeObject(new Student("tt", 18));
			objectwriter.writeObject(new Student("rr", 17));
			objectreader = new ObjectInputStream(new FileInputStream(
					"D:/files/student.txt"));
			for (int i = 0; i < 3; i++) {
				System.out.println(objectreader.readObject());
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			try {
				objectreader.close();
				objectwriter.close();
			} catch (IOException e) {
				// TODO�Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ʱû�б�Ҫ�洢�����������Ϣ����ֻ��Ҫ�洢һ������ĳ�Ա���ݣ���Ա���ݵ����ͼ��趼��Java�Ļ����������ͣ�
	 * ���������󲻱�ʹ�õ���Object����
	 * �������ص������󣬿���ʹ��DataInputStream��DataOutputStream��д���������ݡ�
	 * ������һ�����ӣ���DataInputStream�ĺô������ڴ��ļ���������ʱ�����÷��ĵ������ж϶����ַ���ʱ�����int����ʱ��ʱ��ֹͣ��
	 * ʹ�ö�Ӧ��readUTF()��readInt()�����Ϳ�����ȷ�ض����������������ݡ���
	 */
	static void testDataOutputStream() {
		Member[] members = { new Member("Justin", 90), new Member("momor", 95),
				new Member("Bush", 88) };
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(
					new FileOutputStream("D://aa.txt"));

			for (Member member : members) {
				// д��UTF�ַ���
				dataOutputStream.writeUTF(member.getName());
				// д��int����
				dataOutputStream.writeInt(member.getAge());
			}

			// ����������Ŀ�ĵ�
			dataOutputStream.flush();
			// �ر���
			dataOutputStream.close();

			DataInputStream dataInputStream = new DataInputStream(
					new FileInputStream("D://aa.txt"));

			// �������ݲ���ԭΪ����
			for (int i = 0; i < members.length; i++) {
				// ����UTF�ַ���
				String name = dataInputStream.readUTF();
				// ����int����
				int score = dataInputStream.readInt();
				members[i] = new Member(name, score);
			}

			// �ر���
			dataInputStream.close();

			// ��ʾ��ԭ�������
			for (Member member : members) {
				System.out
						.printf("%s\t%d%n", member.getName(), member.getAge());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * PushbackInputStream��̳���FilterInputStream����iputStream��������ߡ�
	 * �ṩ���Խ����ݲ��뵽������ǰ�˵�����(��ȻҲ��������������)��
	 * �����֮PushbackInputStream������þ����ܹ��ڶ�ȡ��������ʱ����ǰ֪����һ���ֽ���ʲô����ʵ���Ƕ�ȡ����һ���ַ�����˵�������
	 * ��֮����Խ��кܶ���������е�����Ѷ�ȡ�������Ĺ��̵���һ������ı�����������ĳ���ַ���ʱ����Խ��еĲ�������Ȼ�����Ҫ���룬
	 * �ܹ����������ֽ��������ƻػ������Ĵ�С��صģ������ַ��϶����ܴ��ڻ�������
	 * 
	 * @throws IOException
	 */
	static void testPushbackInputStream() throws IOException {
		String str = "hello,rollenholt";
		PushbackInputStream push = null; // ��������������
		ByteArrayInputStream bat = null; // �����ֽ�����������
		bat = new ByteArrayInputStream(str.getBytes());
		push = new PushbackInputStream(bat); // �������������󣬽������ֽ�����������
		int temp = 0;
		while ((temp = push.read()) != -1) { // push.read()���ֽڶ�ȡ�����temp�У������ȡ��ɷ���-1
			if (temp == ',') { // �ж϶�ȡ���Ƿ��Ƕ���
				push.unread(temp); // �ص�temp��λ��
				temp = push.read(); // ���Ŷ�ȡ�ֽ�
				System.out.print("(����" + (char) temp + ") "); // ������˵��ַ�
			} else {
				System.out.print((char) temp); // ��������ַ�
			}
		}
	}

	/**
	 * SequenceInputStream:��Щ����£���������Ҫ�Ӷ���������������������ݡ���ʱ������ʹ�úϲ�����
	 * ������������ϲ���һ��SequenceInputStream������
	 * SequenceInputStream�Ὣ��֮�����ӵ�������ϳ�һ�����������ӵ�һ����������ʼ��ȡ��ֱ�������ļ�ĩβ�����Ŵӵڶ�����������ȡ��
	 * �������ƣ� ֱ��������������һ�����������ļ�ĩβΪֹ�� �ϲ����������ǽ����Դ�ϲ���һ��Դ����ɽ���ö��������յĶ���ֽ�������
	 */
	static void testSequenceInputStream() {
		// ����һ���ϲ����Ķ���
		SequenceInputStream sis = null;
		// �����������
		BufferedOutputStream bos = null;
		try {
			// ���������ϡ�
			Vector<InputStream> vector = new Vector<InputStream>();
			vector.addElement(new FileInputStream("D:\text1.txt"));
			vector.addElement(new FileInputStream("D:\text2.txt"));
			vector.addElement(new FileInputStream("D:\text3.txt"));
			Enumeration<InputStream> e = vector.elements();

			sis = new SequenceInputStream(e);

			bos = new BufferedOutputStream(new FileOutputStream("D:\text4.txt"));
			// ��д����
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = sis.read(buf)) != -1) {
				bos.write(buf, 0, len);
				bos.flush();
			}
		} catch (FileNotFoundException e1) {
		} catch (IOException e1) {
		} finally {
			try {
				if (sis != null)
					sis.close();
			} catch (IOException e) {
			}
			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * �����ַ���������˼�壬���ǲ����ַ��ļ������� 1.java ʹ��Unicode�洢�ַ�������д���ַ���ʱ���Ƕ�����ָ��д����ַ����ı��롣
	 * ǰ������˲������쳣�Ĵ����ֽ������ݵ���ByteArrayOutputStream
	 * ����֮��Ӧ�Ĳ����ַ���������CharArrayReader,CharArrayWriter�࣬
	 * ����Ҳ���õ����������������ַ���������һ�㽲�ַ������뵽�����ַ���io��һ�㷽���� CharArrayReaderreader=mew
	 * CharArrayReader(str.toCharArray());
	 * һ����ȥ��CharArrayReaderʵ���Ϳ���ʹ��CharArrayReader�����ַ����ĸ���Ԫ����ִ�н�һ����ȡ��������������
	 * 2.������FileReader ��PrintWriter����ʾ��
	 */
	static void testFileReaderPrintWriter() {
		char[] buffer = new char[512]; // һ��ȡ�����ֽ�����С,��������С
		int numberRead = 0;
		FileReader reader = null; // ��ȡ�ַ��ļ�����
		PrintWriter writer = null; // д�ַ�������̨����

		try {
			reader = new FileReader("D:/files/copy1.txt");
			writer = new PrintWriter(System.out); // PrintWriter��������ַ����ļ���Ҳ�������������̨
			while ((numberRead = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, numberRead);
			}
		} catch (IOException e) {
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
			writer.close(); // ����������쳣
		}
	}

	/**
	 * 3.�������ǰ���������ֱ����FileReader�򿪵��ļ����������ʹ����������һ��Ƚϳ��õĶ�������������ν���������ǾͶ�ζ����ķ�װ��
	 * �����ܸ��õĲ�������������
	 * ����������������DataInputStream��BufferedInputStream(FileInputStream)��
	 * ���ֽ�������װ�����ǿ��Զ�ȡreadByte
	 * (),readChar()�������Ӿ���Ĳ�����ע�⣬���������ֽ������ַ����в��������ַ�����CharArrayReader�Ϳ�����
	 * �������ʾ�����ǽ��õ�j2se 5�е�һ���ɱ��������һ��С����չ��ʹ��BufferedWriter
	 * ��BufferedReader���ļ������ķ�ʽ����д��
	 * ����������ļ�д�뵽ͬһ�ļ��У��Դ�����������������BufferedReader��BufferedWriter
	 * ,������õ���readLine()�����ˣ���ȡһ�����ݣ�������String����
	 * 
	 * @throws IOException
	 */
	static void testBufferedWriterBufferedReader(String... fileName)
			throws IOException {
		String str;
		// �����Ը��ļ�����������
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"D:/files/copy2.txt"));
		for (String name : fileName) {
			BufferedReader reader = new BufferedReader(new FileReader(name));

			while ((str = reader.readLine()) != null) {
				writer.write(str);
				writer.newLine();
			}
		}
	}

	/**
	 * 4.Console��,�����ṩ�����ڶ�ȡ����ķ��������Խ�ֹ����̨���Բ�����char���飬���������ԶԱ�֤��ȫ�����ã�ƽʱ�õĲ��࣬�˽���С�
	 * 
	 * 5.StreamTokenizer �࣬�����ǳ����ã������԰�����������Ϊ��ǣ�token��, StreamTokenizer
	 * ����������InputStream����OutputStream
	 * �����ǹ�����io���У���ΪStreamTokenizerֻ����InputStream����
	 */
	/**
	 * ʹ��StreamTokenizer��ͳ���ļ��е��ַ��� StreamTokenizer ���ȡ���������������Ϊ����ǡ�������һ�ζ�ȡһ����ǡ�
	 * ����������һ���������������Ϊ����״̬�ı�־���ơ� �����ı������������ʶ���ʶ�������֡����õ��ַ����͸���ע����ʽ��
	 * 
	 * Ĭ������£�StreamTokenizer��Ϊ����������Token: ��ĸ�����֡���C��C++ע�ͷ���������������š�
	 * �����"/"����Token��ע�ͺ������Ҳ���ǣ���"\"��Token�������ź�˫�����Լ����е����ݣ�ֻ������һ��Token��
	 * ͳ�������ַ����ĳ��򣬲��Ǽ򵥵�ͳ��Token�������´󼪣���Ϊ�ַ���������Token������Token�Ĺ涨��
	 * �����е����ݾ�����10ҳҲ��һ��Token�����ϣ�����ź������е����ݶ�����Token��Ӧ�õ�������Ĵ��룺
	 * st.ordinaryChar('\''); st.ordinaryChar('\"');
	 */

	static long testStreamTokenizer() {
		String fileName = "D:/files/copy1.txt";
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileName);
			// �������������ַ����ı��������
			StreamTokenizer st = new StreamTokenizer(new BufferedReader(
					fileReader));

			// ordinaryChar����ָ���ַ������ڴ˱�����������ǡ���ͨ���ַ���
			// ����ָ�������š�˫���ź�ע�ͷ�������ͨ�ַ�
			st.ordinaryChar('\'');
			st.ordinaryChar('\"');
			st.ordinaryChar('/');

			String s;
			int numberSum = 0;
			int wordSum = 0;
			int symbolSum = 0;
			int total = 0;
			// nextToken������ȡ��һ��Token.
			// TT_EOFָʾ�Ѷ�����ĩβ�ĳ�����
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				// �ڵ��� nextToken ����֮��ttype�ֶν������ն�ȡ�ı�ǵ�����
				switch (st.ttype) {
				// TT_EOLָʾ�Ѷ�����ĩβ�ĳ�����
				case StreamTokenizer.TT_EOL:
					break;
				// TT_NUMBERָʾ�Ѷ���һ�����ֱ�ǵĳ���
				case StreamTokenizer.TT_NUMBER:
					// �����ǰ�����һ�����֣�nval�ֶν����������ֵ�ֵ
					s = String.valueOf((st.nval));
					System.out.println("�����У�" + s);
					numberSum++;
					break;
				// TT_WORDָʾ�Ѷ���һ�����ֱ�ǵĳ���
				case StreamTokenizer.TT_WORD:
					// �����ǰ�����һ�����ֱ�ǣ�sval�ֶΰ���һ�����������ֱ�ǵ��ַ����ַ���
					s = st.sval;
					System.out.println("�����У� " + s);
					wordSum++;
					break;
				default:
					// �������3�����Ͷ����ǣ���ΪӢ�ĵı�����
					s = String.valueOf((char) st.ttype);
					System.out.println("����У� " + s);
					symbolSum++;
				}
			}
			System.out.println("������ " + numberSum + "��");
			System.out.println("������ " + wordSum + "��");
			System.out.println("�������У� " + symbolSum + "��");
			total = symbolSum + numberSum + wordSum;
			System.out.println("Total = " + total);
			return total;
		} catch (Exception e) {
			return -1;
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}

class Student implements Serializable {
	private String name;
	private int age;

	public Student(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + "]";
	}
}

class Member {
	private String name;
	private int age;

	public Member() {
	}

	public Member(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}