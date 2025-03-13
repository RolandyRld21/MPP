package MPP.repository;

import MPP.domain.Ticket;

public interface ITicketRepository extends Repository<Long, Ticket>{

    Iterable<Ticket> filterByFlight(Long flightId);
}
