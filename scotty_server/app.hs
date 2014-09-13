{-# LANGUAGE OverloadedStrings, DeriveGeneric #-}
import Web.Scotty
import Data.Aeson (FromJSON, ToJSON) 
import GHC.Generics

data Ok = Ok{success :: Bool} deriving(Show,Generic)

instance FromJSON Ok
instance ToJSON Ok

main = scotty 3000 $ do
  get "/" $ do
    json $ Ok True 

