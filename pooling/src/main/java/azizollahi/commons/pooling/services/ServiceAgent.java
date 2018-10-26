package azizollahi.commons.pooling.services;

import azizollahi.commons.services.Agent;

import java.util.function.Function;

public class ServiceAgent implements Agent<Function, String> {
    @Override
    public String run(Function action) throws InterruptedException {
    	if(action == null)
    		throw new InterruptedException("");
    	action.apply(action);
    	return null;
    }
}
