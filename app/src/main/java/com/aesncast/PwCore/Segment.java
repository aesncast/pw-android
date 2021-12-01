package com.aesncast.PwCore;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Segment {
    public String function;
    public List<SegmentParam> parameters;

    public Segment()
    {
        this("");
    }

    public Segment(String func)
    {
        this.function = func;
        this.parameters = new ArrayList<>();
    }

    public boolean equals(Segment other)
    {
        return this.function.equals(other.function) && this.parameters.equals(other.parameters);
    }

    public Object[] getParamValues(String key, String domain, String user)
    {
        Object[] ret = new Object[this.parameters.size()];

        for (int i = 0; i < this.parameters.size(); ++i) {
            SegmentParam p = this.parameters.get(i);
            switch (p.type) {
                case Field:
                    ret[i] = p.getField(key, domain, user);
                    break;
                case String:
                    ret[i] = p.getString();
                    break;
                case Number:
                    ret[i] = p.getNumber();
                    break;
            }
        }

        return ret;
    }

    private Method findMethod() throws NoSuchMethodException {
        Class<?>[] classes = new Class<?>[this.parameters.size()+1];
        classes[0] = Object.class;
        Class<?>[] param_classes = this.parameters.stream().map(x -> x.getValueClass()).toArray(Class<?>[]::new);
        System.arraycopy(param_classes, 0, classes, 1, param_classes.length);

        try {
            return Transform.class.getMethod(this.function, classes);
        }
        catch (NoSuchMethodException e)
        {
            throw new NoSuchMethodException(String.format("function '%s' does not exist or expects different parameters", this.function));
        }
    }

    public String execute(String acc, String key, String domain, String user) throws NoSuchMethodException {
        Object[] params = this.getParamValues(key, domain, user);
        Method m = this.findMethod();

        Object[] callparams = new Object[params.length+1];
        callparams[0] = acc;
        System.arraycopy(params, 0, callparams, 1, params.length);

        try {
            return (String) m.invoke(null, callparams);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
}
