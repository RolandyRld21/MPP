package common.utils;

import common.business.IService;
import common.jsonprotocol.ProjectClientJsonWorker;

import java.net.Socket;

public class ProjectJsonConcurrentServer extends AbsConcurrentServer{

    private IService projectServer;

    public ProjectJsonConcurrentServer(int port, IService projectServer) {
        super(port);
        this.projectServer = projectServer;
        System.out.println("Project- ProjectJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProjectClientJsonWorker worker = new ProjectClientJsonWorker(projectServer, client);

        Thread tw = new Thread(worker);
        return tw;
    }
}
