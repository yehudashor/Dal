import groovy.lang.Range;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HelpFunction
{
    public static <T> void PrintCollection(Collection<T> collection)
    {
        //collection.stream().forEach(System.out::print);
        collection.stream().forEach(x -> System.out.println(x.toString()));
    }
}

//foreach (var item in Enumerable.Range(0, orderInfo.Length).Where(x => orderInfo[x] == ':'))
//{
//    string s = new string(orderInfo.Skip(item + 2).ToArray());
//    var t = new string(s.TakeWhile(i => !string.IsNullOrWhiteSpace(i.ToString())).ToArray());
//    vs1.Add(t);
//}