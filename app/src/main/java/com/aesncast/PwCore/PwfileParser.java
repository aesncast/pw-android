package com.aesncast.PwCore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PwfileParser {
    static final String forbiddenSymbols = ":;,<>[]";

    private static class ParseResult<T>
    {
        public int i;
        public T result;

        public ParseResult(int i, T result)
        {
            this.i = i;
            this.result = result;
        }
    }

    private static Sequence get_good_password_sequence()
    {
        Sequence s = new Sequence("good_password");
        Segment s1 = new Segment("init");
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "key"));
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "domain"));
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "user"));
        s.segments.add(s1);

        Segment s2 = new Segment("diceware");
        s2.parameters.add(new SegmentParam(SegmentParam.ParamType.Number, 4));
        s2.parameters.add(new SegmentParam(SegmentParam.ParamType.Number, 4));
        s.segments.add(s2);

        Segment s3 = new Segment("capitalize_some");
        s.segments.add(s3);

        Segment s4 = new Segment("add_some_simple_special_characters");
        s.segments.add(s4);

        return s;
    }

    public static Pwfile parsePwlist4(String source)
    {
        Pwfile ret = new Pwfile();

        String[] lines = source.split("\n");

        if (lines.length == 0)
            return null;

        int i = 0;

        while (i < lines.length)
        {
            String line = lines[i];

            if (line.trim().isEmpty())
            {
                i += 1;
                continue;
            }

            if (line.charAt(0) == '#')
            {
                i += 1;
                continue;
            }

            try
            {
                if (line.charAt(0) == '[')
                {
                    // sequence
                    ParseResult<Sequence> res = parsePwlist4Sequence(lines, i);
                    Sequence seq = res.result;

                    if (ret.sequences.containsKey(seq.name))
                        throw new ParseException(String.format("duplicate sequence name '%s'", seq.name), i);

                    if (seq.is_default)
                    {
                        if (!ret.default_sequence_name.isEmpty())
                            throw new ParseException(String.format("multiple default sequences not allowed: '%s' and '%s' are both marked default", seq.name, ret.default_sequence_name), i);
                    }

                    ret.sequences.put(seq.name, seq);
                    i = res.i;
                }
                else if (" \t".indexOf(line.charAt(0)) == -1)
                {
                    ParseResult<PwDomain> res = parsePwlist4Domain(lines, i);
                    PwDomain dom = res.result;

                    if (ret.domains.containsKey(dom.name))
                        throw new ParseException(String.format("duplicate domain name '%s'", dom.name), i);

                    ret.domains.put(dom.name, dom);
                    i = res.i;
                }
                else
                    throw new ParseException(String.format("unexpected symbol '%s'", line.charAt(0)), i);
            }
            catch (ParseException e)
            {
                // TODO: log error message
                System.out.println(e.getMessage());
                i += 1;
            }
        }

        if (!ret.sequences.containsKey("good_password"))
            ret.sequences.put("good_password", get_good_password_sequence());

        if (ret.default_sequence_name.isEmpty())
            ret.default_sequence_name = "good_password";

        return ret;
    }

    private static ParseResult<Sequence> parsePwlist4Sequence(String[] lines, int i) throws ParseException {
        Sequence seq = new Sequence("");

        seq.name = parsePwlist4SequenceName(lines[i]);

        if (seq.name.startsWith("+")) {
            seq.is_default = true;
            seq.name = seq.name.substring(1);

            if (seq.name.isEmpty())
                throw new ParseException("sequence name cannot be empty", i);
        }

        i += 1;
        int j = i;

        while (i < lines.length && lines[i] != "\n")
            i += 1;

        Stream<String> seglines = Arrays.stream(lines).skip(j).limit(i-j);
        String segstr = String.join(" ", seglines.map(p -> p.trim()).toArray(String[]::new));

        seq.segments = parsePwlist4SequenceSegments(segstr);

        return new ParseResult<>(i, seq);
    }

    private static List<Segment> parsePwlist4SequenceSegments(String s) throws ParseException {
        List<Segment> ret = new ArrayList<>();

        int i = 0;
        while (i < s.length())
        {
            char c = s.charAt(i);

            if (Character.isWhitespace(c))
            {
                i += 1;
                continue;
            }

            ParseResult<Segment> result = parsePwlist4SequenceSegment(s, i);

            i = result.i;
            ret.add(result.result);
        }

        return ret;
    }

    private static ParseResult<Segment> parsePwlist4SequenceSegment(String s, int i) throws ParseException {
        i = parseSkipWhitespace(s, i);
        ParseResult<String> rfunc = parsePwlist4Identifier(s, i);
        i = rfunc.i;
        ParseResult<List<SegmentParam>> rparam = parsePwlist4SegmentParams(s, i);
        i = rparam.i;

        Segment ret = new Segment(rfunc.result);
        ret.parameters = rparam.result;

        return new ParseResult<>(i, ret);
    }

    private static ParseResult<List<SegmentParam>> parsePwlist4SegmentParams(String s, int i) throws ParseException {
        List<SegmentParam> ret = new ArrayList<>();
        i = parseSkipWhitespace(s, i);

        if (i >= s.length())
            throw new ParseException("expected symbol '(', got EOF in segment parameters", -1);

        if (s.charAt(i) != '(')
            throw new ParseException(String.format("expected symbol '(', got '%s' in segment parameters", s.charAt(i)), -1);

        i += 1;

        while (i < s.length())
        {
            char c = s.charAt(i);

            if (Character.isWhitespace(c))
            {
                i += 1;
                continue;
            }

            if (c == ')')
                break;

            ParseResult<SegmentParam> res = parsePwlist4SegmentParam(s, i);
            ret.add(res.result);
            i = res.i;

            int j = parseSkipWhitespace(s, i);

            if (j < s.length())
            {
                if (s.charAt(j) == ')')
                    continue;

                if (s.charAt(j) == ',')
                {
                    i = j + 1;
                    continue;
                }

                throw new ParseException(String.format("expected symbol ')' or ',', got '%s' in segment parameters", s.charAt(j)), -1);
            }
        }

        if (i >= s.length() || s.charAt(i) != ')')
            throw new ParseException("expected symbol ')', got EOF in segment parameters", -1);

        i += 1;

        return new ParseResult<>(i, ret);
    }

    private static ParseResult<SegmentParam> parsePwlist4SegmentParam(String s, int i) throws ParseException {
        SegmentParam ret = new SegmentParam(SegmentParam.ParamType.Field, "");
        i = parseSkipWhitespace(s, i);

        if (i >= s.length())
            throw new ParseException("expected symbol, got EOF in segment parameter", -1);

        char c = s.charAt(i);

        if (c == '$')
        {
            ParseResult<String> res = parsePwlist4Identifier(s, i+1);
            ret.type = SegmentParam.ParamType.Field;
            ret.value = res.result;
            i = res.i;
        }
        else if (Character.isDigit(c))
        {
            ParseResult<Integer> res = parseNumber(s, i);
            ret.type = SegmentParam.ParamType.Number;
            ret.value = res.result;
            i = res.i;
        }
        else
        {
            ParseResult<String> res = parseString(s, i);
            ret.type = SegmentParam.ParamType.String;
            ret.value = res.result;
            i = res.i;
        }

        return new ParseResult<>(i, ret);
    }

    private static ParseResult<String> parseString(String s, int i) throws ParseException {
        String ret = "";

        if (i >= s.length())
            throw new ParseException("expected string in segment", -1);

        char c = s.charAt(i);

        if (c != '"')
            throw new ParseException(String.format("undexpected symbol '%s' in string", c), -1);

        i += 1;

        while (i < s.length())
        {
            c = s.charAt(i);

            if (c == '"')
                break;

            ret += c;
            i += 1;
        }

        if (i >= s.length() || s.charAt(i) != '"')
            throw new ParseException("expected '\"' to terminate string", -1);

        return new ParseResult<>(i, ret);
    }

    private static ParseResult<Integer> parseNumber(String s, int i) throws ParseException {
        String ret = "";

        if (i >= s.length())
            throw new ParseException("expected identifier in segment", -1);

        char c = s.charAt(i);

        if (!Character.isDigit(c))
            throw new ParseException(String.format("undexpected symbol '%s' in number", c), -1);

        ret += c;
        i += 1;

        while (i < s.length())
        {
            c = s.charAt(i);

            if (Character.isDigit(c))
            {
                ret += c;
                i += 1;
                continue;
            }
            else
                break;
        }

        return new ParseResult<>(i, Integer.valueOf(ret));
    }

    private static ParseResult<String> parsePwlist4Identifier(String s, int i) throws ParseException {
        String ret = "";

        if (i >= s.length())
            throw new ParseException("expected identifier in segment", -1);

        char c = s.charAt(i);

        if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(c) == -1)
            throw new ParseException(String.format("undexpected symbol '%s' in identifier", c), -1);

        ret += c;
        i += 1;

        while (i < s.length())
        {
            c = s.charAt(i);

            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".indexOf(c) >= 0)
            {
                ret += c;
                i += 1;
                continue;
            }
            else
            {
                break;
            }
        }

        return new ParseResult<>(i, ret);
    }

    private static int parseSkipWhitespace(String s, int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i)))
            i += 1;

        return i;
    }

    private static String parsePwlist4SequenceName(String line) throws ParseException {
        int begin = line.indexOf('[');
        int end = line.indexOf(']');

        if (begin < 0)
            throw new ParseException("couldn't find symbol [ in sequence name", -1);

        if (end < 0)
            throw new ParseException("couldn't find symbol ] in sequence name", -1);

        if (end < begin)
            throw new ParseException("] must come after [ in sequence name", -1);

        String name = line.substring(begin+1, end);
        checkForbiddenSymbolInName(name);
        return name;
    }

    private static ParseResult<PwDomain> parsePwlist4Domain(String[] lines, int i) throws ParseException {
        PwDomain dom = new PwDomain("");
        dom.name = parsePwlist4DomainName(lines[i]);

        i += 1;

        while (i < lines.length)
        {
            String line = lines[i];

            if (line.trim().isEmpty()) {
                i += 1;
                continue;
            }

            if (!line.startsWith("\t") && !line.startsWith(" "))
                break;

            line = line.trim();

            if (line.startsWith("-"))
                line = " " + line;

            String[] parts = Arrays.stream(line.split(" - ")).map(p -> p.trim()).toArray(String[]::new);

            if (parts.length != 2)
                throw new ParseException(String.format("invalid user entry '%s' in domain '%s'", line, dom.name), i);

            String usrname = parts[0];
            String seqname = parts[1];

            checkForbiddenSymbolInName(usrname);
            checkForbiddenSymbolInName(seqname);

            PwUser usr = new PwUser(usrname, seqname);
            dom.users.put(usrname, usr);

            i += 1;
        }

        return new ParseResult<>(i, dom);
    }

    private static String parsePwlist4DomainName(String line) throws ParseException {
        String name = line.trim();

        if (name.isEmpty() || !name.endsWith(":"))
            throw new ParseException("domain name must end with ':'", -1);

        name = line.substring(0, name.length()-1);
        checkForbiddenSymbolInName(name);

        return name;
    }

    private static void checkForbiddenSymbolInName(String name) throws ParseException {
        for (char sb : forbiddenSymbols.toCharArray())
            if (name.indexOf(sb) >= 0)
                throw new ParseException(String.format("illegal symbol '%s' in name '%s'", sb, name), -1);
    }
}
