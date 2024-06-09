package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());

    // Lista di pagine consentite
    private static final String[] ALLOWED_PAGES = { "Home.jsp"};

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ProdottoDao dao = new ProdottoDao();
        
        ArrayList<ArrayList<ProdottoBean>> categorie = new ArrayList<>();
        String redirectedPage = request.getParameter("page");

        // Logging del parametro page
        LOGGER.log(Level.INFO, "Requested page: {0}", redirectedPage);

        // Validazione del parametro page
        if (isAllowedPage(redirectedPage)) {
            try {
                ArrayList<ProdottoBean> PS5 = dao.doRetrieveByPiattaforma("PlayStation 5");
                ArrayList<ProdottoBean> XboxSeries = dao.doRetrieveByPiattaforma("Xbox Series");
                ArrayList<ProdottoBean> Switch = dao.doRetrieveByPiattaforma("Nintendo Switch");
                ArrayList<ProdottoBean> PS4 = dao.doRetrieveByPiattaforma("PlayStation 4");
                ArrayList<ProdottoBean> Xone = dao.doRetrieveByPiattaforma("Xbox One");
                
                categorie.add(PS5);
                categorie.add(XboxSeries);
                categorie.add(Switch);
                categorie.add(PS4);
                categorie.add(Xone);

                request.getSession().setAttribute("categorie", categorie);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + redirectedPage);
            dispatcher.forward(request, response);
        } else {
            // Logging del tentativo di accesso non autorizzato
            LOGGER.log(Level.WARNING, "Unauthorized access attempt to page: {0}", redirectedPage);

            // Se la pagina richiesta non è consentita, reindirizza alla homepage o a una pagina di errore
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // Metodo per verificare se il file richiesto è consentito
    private boolean isAllowedPage(String page) {
        if (page == null) {
            return false;
        }
        for (String allowedPage : ALLOWED_PAGES) {
            if (page.equals(allowedPage)) {
                return true;
            }
        }
        return false;
    }
}