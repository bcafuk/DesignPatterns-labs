add_executable(lab3zad1_1 zad1_1.c myfactory.h myfactory.c)

add_library(parrot SHARED parrot.c)
add_library(tiger SHARED tiger.c)

# Optionally can be used to avoid having to compile targets separately
# add_dependencies(lab3zad1_1 parrot tiger)
