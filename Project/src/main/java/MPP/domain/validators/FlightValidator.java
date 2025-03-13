package MPP.domain.validators;
import MPP.domain.Flight;
import MPP.exceptions.ValidationException;
import java.util.Objects;

public class FlightValidator implements Validator<Flight> {
    private static final FlightValidator instance = new FlightValidator();
    public static FlightValidator getInstance() {
        return instance;
    }
    private FlightValidator() {
    }
    @Override
    public void validate(Flight flight) throws ValidationException {
        String errors = "";
        if(Objects.equals(flight.getFrom(),""))
            errors += "Departure ca not be empty";
        if(Objects.equals(flight.getTo(),""))
            errors += "Departure ca not be empty";
        if (flight.getNrOfSeats() < 0)
            errors += "Number of seats must be greater than 0";
        if(!errors.isEmpty())
            throw new ValidationException(errors);

    }
}
