package MPP.domain.validators;
import MPP.domain.Ticket;
import MPP.exceptions.ValidationException;
import java.util.Objects;

public class TicketValidator implements Validator<Ticket> {
    private static TicketValidator instance  = new TicketValidator();
    public static TicketValidator getInstance() {
        return instance;
    }
    public TicketValidator() {
    }
    @Override
    public void validate(Ticket entity) throws ValidationException {
        String errors = "";
        if(entity.getNrSeats() <= 0)
            errors += "NrSeats must be greater than 0.";
        if (Objects.equals(entity.getClientName(),""))
            errors += "Client name is required.";
       
        if (Objects.equals(entity.getAddress(),""))
            errors += "Address is required.";
        if(!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
