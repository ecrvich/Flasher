package org.ecrvich.Flasher;

public class IntNode
extends Node
{
   private int val;
   
   public IntNode( int val )
   {
      this.val = val;      
   }

   @Override
   public <T extends Node> int compare( T obj )
   {
      if (obj instanceof IntNode)
         return (compare( (IntNode)obj ));
      return (5);
   }

   public int compare( IntNode obj ) 
   {
      if (this.val < obj.val)
         return (-1);
      else if (this.val > obj.val)
         return (1);
      else
         return (0);
   }
}
