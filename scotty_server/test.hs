{-# LANGUAGE EmptyDataDecls       #-}
{-# LANGUAGE FlexibleContexts     #-}
{-# LANGUAGE FlexibleInstances    #-}
{-# LANGUAGE GADTs                #-}
{-# LANGUAGE OverloadedStrings    #-}
{-# LANGUAGE QuasiQuotes          #-}
{-# LANGUAGE TemplateHaskell      #-}
{-# LANGUAGE TypeFamilies         #-}
{-# LANGUAGE TypeSynonymInstances #-}
{-# OPTIONS_GHC -fno-warn-orphans #-}
import Web.Scotty
import Text.Blaze.Html5 hiding (map)
import Text.Blaze.Html5.Attributes
import qualified Web.Scotty as S
import qualified Text.Blaze.Html5 as H
import qualified Text.Blaze.Html5.Attributes as A
import Text.Blaze.Html.Renderer.Text
import Database.Persist
import Database.Persist.Sqlite
import Database.Persist.TH
import Data.Text (Text)
import Data.Time (UTCTime, getCurrentTime)
import qualified Data.Text as T
import Control.Monad.IO.Class (liftIO)
import Control.Monad.Trans.Resource (runResourceT, ResourceT)
import Database.Persist.GenericSql
import Control.Monad (forM_)
import Control.Applicative
import Control.Monad.Logger
 
share [mkPersist sqlSettings, mkMigrate "migrateAll"] [persist|
Post
    title String
    content Text
    createdAt UTCTime
    deriving Show
|]
 
runDb :: SqlPersist (ResourceT IO) a -> IO a
runDb query = runResourceT . withSqliteConn "dev.sqlite3" . runSqlConn $ query
 
readPosts :: IO [Entity Post]
readPosts = (runDb $ selectList [] [LimitTo 10])
 
blaze = S.html . renderHtml
 
main = do
  runDb $ runMigration migrateAll    
  scotty 3000 $ do
    S.get "/create/:title" $ do
      _title <- S.param "title"
      now <- liftIO getCurrentTime
      liftIO $ runDb $ insert $ Post _title "some content" now
      S.redirect "/"
 
    S.get "/" $ do
      _posts <- liftIO readPosts
      let posts = map (postTitle . entityVal) _posts
      blaze $ do
        ul $ do
          forM_ posts $ \post -> li (toHtml post)
