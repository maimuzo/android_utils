class User < ActiveRecord::Base
  validates_presence_of :key, :name
  validates_uniqueness_of :key
end
