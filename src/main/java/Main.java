import controller.InferenceController;
import core.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.ModelService;
import service.PreprocessService;

public class Main {

    public static void main(String[] args) throws Exception{
        // 사전 / 불용어 / 모델 목록 로드
        Dictionary dict = Dictionary.load("DICTIONARY.txt");
        StopWords stop = StopWords.load("./STOPWORD.txt");
        Models models = Models.load("./MODELS.json");

        Preprocessor preprocessor = new Preprocessor(dict, stop);
        PreprocessService preprocessService = new PreprocessService(preprocessor);
        ModelInvoker invoker = new ModelInvoker();
        ModelService modelService = new ModelService(models, invoker);

        InferenceController controller = new InferenceController(preprocessService, modelService);

        // Jetty 서버 (127.0.0.1:8080 바인딩)
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost("127.0.0.1");
        http.setPort(8080);
        server.setConnectors(new ServerConnector[]{http});

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ctx.setContextPath("/");
        ctx.addServlet(new ServletHolder(controller), "/"); // Controller 등록
        server.setHandler(ctx);

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
