import controller.InferenceController;
import controller.KeywordSearchController;
import controller.MockController;
import core.*;
import infra.db.Db;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.MockService;
import service.ModelService;
import service.PreprocessService;
import service.SearchService;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception{
        // 사전 / 불용어 / 모델 목록 로드
        System.out.println("CWD = " + Paths.get(".").toAbsolutePath());
        System.out.println("Looking for = " + Paths.get("DICTIONARY.txt").toAbsolutePath());
        Dictionary dict = Dictionary.load("DICTIONARY.txt");
        StopWords stop = StopWords.load("STOPWORD.txt");
        Models models = Models.load("MODELS.json");

        Db.init();

        Preprocessor preprocessor = new Preprocessor(dict, stop);
        PreprocessService preprocessService = new PreprocessService(preprocessor);
        ModelInvoker invoker = new ModelInvoker();
        ModelService modelService = new ModelService(models, invoker);

        InferenceController controller = new InferenceController(preprocessService, modelService);

        try(ExternalApiClient client = new ExternalApiClient()) {
            SearchService searchService = new SearchService(client);

            KeywordSearchController keywordController = new KeywordSearchController(searchService);

            MockService mockService = new MockService(client);
            MockController mockController = new MockController(mockService);

            // Jetty 서버 (127.0.0.1:8080 바인딩)
            Server server = new Server();
            ServerConnector http = new ServerConnector(server);
            http.setHost("127.0.0.1");
            http.setPort(8080);
            server.setConnectors(new ServerConnector[]{http});

            ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            ctx.setContextPath("/");
            ctx.addServlet(new ServletHolder(controller), "/"); // Controller 등록
            ctx.addServlet(new ServletHolder(keywordController), "/api/v1/search/*");
            ctx.addServlet(new ServletHolder(mockController), "/api/v1/mock/*");
            server.setHandler(ctx);

            // 3) Ctrl+C 등 종료 시 Jetty 먼저 멈추기
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try { server.stop(); } catch (Exception ignored) {}
            }));

//        ctx.setContextPath("/api/v1"); // 공통 prefix
//
//        ctx.addServlet(new ServletHolder(new DeployController()),   "/deploy/*");
//        ctx.addServlet(new ServletHolder(new FineTuneController()), "/fine-tune/*");
//
//        server.setHandler(ctx);

//        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//        ctx.setContextPath("/");
//
//        ctx.addServlet(new ServletHolder(new DeployController()),   "/api/v1/deploy/*");
//        ctx.addServlet(new ServletHolder(new FineTuneController()), "/api/v1/fine-tune/*");

            server.start();
            server.join();
        }




    }
}
