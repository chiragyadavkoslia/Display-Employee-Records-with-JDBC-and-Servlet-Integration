import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // JDBC credentials
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/CompanyDB";
    private final String JDBC_USER = "root";       // replace with your DB username
    private final String JDBC_PASSWORD = "password"; // replace with your DB password

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("empid");

        out.println("<html><body>");
        out.println("<h2>Employee Details</h2>");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String query;
            PreparedStatement ps;

            if (empIdParam != null && !empIdParam.isEmpty()) {
                // Search for specific employee
                query = "SELECT * FROM Employee WHERE EmpID = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(empIdParam));
            } else {
                // Fetch all employees
                query = "SELECT * FROM Employee";
                ps = conn.prepareStatement(query);
            }

            ResultSet rs = ps.executeQuery();

            out.println("<table border='1'>");
            out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");

                out.println("<tr><td>" + id + "</td><td>" + name + "</td><td>" + salary + "</td></tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='3'>No employee found.</td></tr>");
            }

            out.println("</table>");

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        out.println("</body></html>");
        out.close();
    }
}
