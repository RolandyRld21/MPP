package server.repository;

import common.domain.Agent;

import java.util.Optional;
import java.util.UUID;

public interface IAgentRepository extends Repository<Long, Agent> {

    Agent findOne(UUID identifier);

    Agent delete(UUID identifier);

    Agent tryLogin(String username, String password);

    Optional<Agent> findOneByUsername(String username);
}
