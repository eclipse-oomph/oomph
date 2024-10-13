package org.eclipse.oomph.setup.maven.util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertM2RepositoryToUTF8Handler extends AbstractHandler
{
  @SuppressWarnings("nls")
  private static final Pattern ISO_LATIN = Pattern.compile("^([^>]+encoding=['\"])ISO-8859-1(['\"])", Pattern.CASE_INSENSITIVE);

  @SuppressWarnings("nls")
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    Path m2 = Path.of(System.getProperty("user.home")).resolve(".m2");
    if (Files.isDirectory(m2))
    {
      try
      {
        Files.walkFileTree(m2, new SimpleFileVisitor<Path>()
        {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
          {
            if (file.getFileName().toString().endsWith(".pom"))
            {
              try
              {
                String string = Files.readString(file, StandardCharsets.ISO_8859_1);
                Matcher matcher = ISO_LATIN.matcher(string);
                if (matcher.find())
                {
                  System.out.println("Converting " + file);
                  String utf8 = matcher.replaceFirst("$1UTF-8$2"); //$NON-NLS-1$
                  Files.writeString(file, utf8, StandardCharsets.UTF_8);
                }
              }
              catch (IOException ex)
              {
                // Assume the file is not ISO_8859_1 compatible.
                // $FALL-THROUGH$
              }
            }

            return super.visitFile(file, attrs);
          }
        });
      }
      catch (IOException ex)
      {
        throw new ExecutionException(ex.getMessage(), ex);
      }
    }

    return null;
  }

}
