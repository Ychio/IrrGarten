require_relative 'Dice'
module Irrgarten
  class Shield
    def initialize(protection,uses)
      @protection = protection
      @uses = uses
    end

    def protect
      if @uses > 0
       @uses -= 1
       @protection.to_f
      else
        @uses.to_f
      end
    end

    def to_s
      "W[" + @protection.to_s + "," + @uses.to_s + "]"
    end

    def discard
      Dice.discard_element(@uses)
    end
  end
end