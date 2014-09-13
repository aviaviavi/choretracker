module Paths_chore (
    version,
    getBinDir, getLibDir, getDataDir, getLibexecDir,
    getDataFileName
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
catchIO = Exception.catch


version :: Version
version = Version {versionBranch = [0,0,1], versionTags = []}
bindir, libdir, datadir, libexecdir :: FilePath

bindir     = "/home/avi/.cabal/bin"
libdir     = "/home/avi/.cabal/lib/chore-0.0.1/ghc-7.6.3"
datadir    = "/home/avi/.cabal/share/chore-0.0.1"
libexecdir = "/home/avi/.cabal/libexec"

getBinDir, getLibDir, getDataDir, getLibexecDir :: IO FilePath
getBinDir = catchIO (getEnv "chore_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "chore_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "chore_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "chore_libexecdir") (\_ -> return libexecdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
