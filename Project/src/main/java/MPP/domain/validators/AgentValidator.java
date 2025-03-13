package MPP.domain.validators;
import MPP.domain.Agent;
import MPP.exceptions.ValidationException;
import java.util.Objects;

public class AgentValidator implements Validator<Agent> {
    private static final AgentValidator instance = new AgentValidator();
    public static AgentValidator getInstance() {
        return instance;
    }
    private AgentValidator() {
    }
    @Override
    public void validate(Agent entity) throws ValidationException {
        String errors = "";
        if(Objects.equals(entity.getUsername(),""))
            errors += "Username can not be empty!\n";
        if(Objects.equals(entity.getPassword(),""))
            errors += "Password can not be empty!\n";
        if(!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
