package MPP.repository;

import MPP.domain.Agent;

import java.util.Optional;

public interface IAgentRepository extends Repository<Long, Agent> {

    Optional<Agent> findOneByUsername(String username);
}
