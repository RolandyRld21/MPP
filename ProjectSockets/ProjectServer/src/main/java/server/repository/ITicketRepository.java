package server.repository;

import common.domain.Ticket;

public interface ITicketRepository extends Repository<Long, Ticket> {

    Iterable<Ticket> filterByFlight(Long flightId);
}
