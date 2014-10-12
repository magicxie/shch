package net.sc.pd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PredictionController {

	@Autowired
	GreyModel corePredictor;
	
    @RequestMapping("/predict")
    public @ResponseBody double greeting(@RequestParam(value="sample", required=true, defaultValue="World") double[] sample,
    		@RequestParam(value="next", required=true, defaultValue="1") int next,
    		Model model) {
    	
        return corePredictor.resolve(sample, next);
    }
    
    @RequestMapping("/predictBatch")
    public @ResponseBody double[] predict(@RequestParam(value="sample", required=true, defaultValue="World") double[] sample,
    		@RequestParam(value="next", required=true, defaultValue="1") int next,
    		Model model) {
    	
    	double[] result = new double[next];
    	
    	if(next > 0){
    		for(int i = 1;i < next+1; i++){
    			result[i-1] = corePredictor.resolve(sample, i);
    		}
    	}
    	
        return result;
    }

}