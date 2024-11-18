import java.util.Map;

public interface IResourceHandler {
    ResponceContent handle(Map<String, String> params, String someOtherParam);
}



