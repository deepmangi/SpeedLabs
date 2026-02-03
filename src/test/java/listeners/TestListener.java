package listeners;

//import globalSetup.EmailSender;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;

public class TestListener implements Plugin, ConcurrentEventListener {

    public TestListener() {
    }
    

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, event -> {
            System.out.println("Test Run Started");
        });
        
        
        
        publisher.registerHandlerFor(TestRunFinished.class, event -> {
//        	EmailSender.sendTestReportEmail();
            System.out.println("Test Run Finished");
        });
    }
}