import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    // JDBC configuration
    private static final String URL = "jdbc:mysql://localhost:3306/companydb";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";  // 🔒 replace with your MySQL password

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("empid");
        String viewAllParam = request.getParameter("viewAll");

        out.println("<html><body>");
        out.println("<h2>Employee Details</h2>");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps;
            ResultSet rs;

            if (empIdParam != null && !empIdParam.isEmpty()) {
                // --- Search specific employee by ID ---
                ps = con.prepareStatement("SELECT * FROM Employee WHERE EmpID = ?");
                ps.setInt(1, Integer.parseInt(empIdParam));
                rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<table border='1'>");
                    out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
                    out.println("<tr><td>" + rs.getInt("EmpID") + "</td>");
                    out.println("<td>" + rs.getString("Name") + "</td>");
                    out.println("<td>" + rs.getDouble("Salary") + "</td></tr>");
                    out.println("</table>");
                } else {
                    out.println("<p style='color:red;'>No employee found with ID " + empIdParam + ".</p>");
                }

            } else if (viewAllParam != null) {
                // --- Display all employees ---
                ps = con.prepareStatement("SELECT * FROM Employee");
                rs = ps.executeQuery();

                out.println("<table border='1'>");
                out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("EmpID") + "</td>");
                    out.println("<td>" + rs.getString("Name") + "</td>");
                    out.println("<td>" + rs.getDouble("Salary") + "</td></tr>");
                }
                out.println("</table>");

            } else {
                out.println("<p>Please use the form to search or view all employees.</p>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("<br><a href='employee.html'>Back to Search</a>");
        out.println("</body></html>");
    }
}
