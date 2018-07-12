package ru.unlimit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/Phone/*")
public class Phone extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String, LinkedList<String>> table = new ConcurrentHashMap<>();

	public void init() {
		Scanner input;
		try {
			input = new Scanner(
					new FileReader("C:\\Users\\Роман\\Documents\\7\\Phone_books\\src\\ru\\unlimit\\Phone.txt"));
			while (input.hasNextLine()) {
				String name_f = input.next();
				Scanner phones = new Scanner(input.nextLine());
				LinkedList<String> phone = new LinkedList<>();
				while (phones.hasNext())
					phone.add(phones.next());
				table.put(name_f, phone);
				phones.close();
			}
		} catch (FileNotFoundException e) {
		}
	}

	/* save to file */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Do get ");
		request.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();

		response.setContentType("text/html;charset=utf-8"); // translate to
															// Russian
		PrintWriter out = response.getWriter();// output in web-page

		// System.out.println("uri: " + uri);
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Phone</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<p> <a href=\"http://localhost:17017/Phone_books/addUser.html\"> Add User (or Number) </a></p>");
		out.println(
				"<p> <a href=\"http://localhost:17017/Phone_books/DelUser.html\"> Remove User (or Number) </a></p>");
		out.println("<form action=\"save\"><p>");
		out.println("<p> <input type=\"submit\" value=\"Save to file\"</p>");
		out.println("</p></form>");

		if (uri.equals("/Phone_books/Phone/addUser")) {
			// System.out.println("Add ");
			String log = request.getParameter("log");
			String tel = request.getParameter("tel");
			Add(log, tel);
		}
		if (uri.equals("/Phone_books/Phone/save")) {
			FileWriter out_file = new FileWriter(
					"C:\\Users\\Роман\\Documents\\7\\Phone_books\\src\\ru\\unlimit\\Phone.txt");
			try {
				for (Map.Entry<String, LinkedList<String>> entry : table.entrySet()) {
					String str = new String();
					str = entry.getKey() + " ";
					ListIterator<String> iter = entry.getValue().listIterator();
					while (iter.hasNext())
						str = str + iter.next() + "  ";
					str += "\n";
					out_file.write(str);
				}
			} catch (IOException e) {
				System.out.print("File is saved");
			}
			out_file.close();
		}
		if (uri.equals("/Phone_books/Phone/delUser")) {
			// System.out.println("Del ");
			String log = request.getParameter("log");
			if (!table.containsKey(log)) {
				System.out.println("Not found");
				Show(out);
				out.println("</body>");
				out.println("</html>");
				out.close();
				return;
			}
			String tel = request.getParameter("tel");
			Del(log, tel);
		}

		Show(out);
		out.println("</body>");
		out.println("</html>");
		out.close();

	}

	private void Add(String name, String phone) {
		//if(!table.get(name).contains(phone))
		if (table.containsKey(name)) {
			table.get(name).add(phone);
		} else {
			LinkedList<String> node = new LinkedList<>();
			node.add(phone);
			table.put(name, node);
		}
	}

	private void Del(String name, String phone) {
		if (phone == "") {
			table.remove(name);
			return;
		} else {
			LinkedList<String> list = table.get(name);
			list.remove(phone);
			table.remove(name);
			table.put(name, list);
		}
	}

	private void Show(PrintWriter out) throws IOException {
		for (Map.Entry<String, LinkedList<String>> entry : table.entrySet()) {
			String str = new String();
			str = "<p><i><b>" + entry.getKey() + "</b></i> : ";
			ListIterator<String> iter = entry.getValue().listIterator();
			while (iter.hasNext())
				str = str + iter.next() + " ";
			str += "</p>";
			out.println(str);
			// System.out.println(str);
		}
	}
}
