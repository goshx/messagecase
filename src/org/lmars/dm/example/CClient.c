#include <stdio.h>      /* for printf() and fprintf() */
#include <sys/socket.h> /* for socket(), connect(), send(), and recv() */
#include <arpa/inet.h>  /* for sockaddr_in and inet_addr() */
#include <netinet/tcp.h>
#include <stdlib.h>     /* for atoi() and exit() */
#include <string.h>     /* for memset() */
#include <unistd.h>     /* for close() */
#include <sys/time.h>

#define SWAP16(s) ((((s) & 0xff) << 8) | (((s) >> 8) & 0xff))
#define SWAP32(l) (((l) >> 24) | \
    (((l) & 0x00ff0000) >> 8)  | \
    (((l) & 0x0000ff00) << 8)  | \
    ((l) << 24))
#define SWAP64(ll) (((ll) >> 56) |\
    (((ll) & 0x00ff000000000000) >> 40) |\
    (((ll) & 0x0000ff0000000000) >> 24) |\
    (((ll) & 0x000000ff00000000) >> 8)  |\
    (((ll) & 0x00000000ff000000) << 8)  |\
    (((ll) & 0x0000000000ff0000) << 24) |\
    (((ll) & 0x000000000000ff00) << 40) |\
    (((ll) << 56)))

typedef struct _MHEAD {
    unsigned int _type_;
    unsigned int _id_len_;
    char*        _id_;
    unsigned int _project_len_;
    char*        _project_;
    unsigned int _topic_len_;
    char*        _topic_;
} mhead;

typedef struct _MFRAM {
    mhead _head_;
    char  _content_[400];
} mfram;

mfram __login() {
    mfram _fram;
    return _fram;
}

mfram __subscribe() {
    mfram _fram;
    return _fram;
}

mfram __publish() {
    mfram _fram;
    return _fram;
}

void __die(char *_err)
{
    printf("Error:%s",_err);
    getchar();
    exit(1);
}

void __handle(char * _host, unsigned short _port, char * _c_msg) {
    int _sock;                        /* Socket descriptor */
    struct sockaddr_in _addr;         /* Echo server address */
    char * _o_msg;

    int value = 1;

    /* Create a reliable, stream socket using TCP */
    if ((_sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
        __die("socket() failed");

    /* Construct the server address structure */
    memset(&_addr, 0, sizeof(_addr));             /* Zero out structure */
    _addr.sin_family      = AF_INET;              /* Internet address family */
    _addr.sin_addr.s_addr = inet_addr(_host);     /* Server IP address */
    _addr.sin_port        = htons(_port);         /* Server port */

    /* Establish the connection to the echo server */
    if (connect(_sock, (struct sockaddr *) &_addr, sizeof(_addr)) < 0)
        __die("connect() failed");

    if (setsockopt(_sock, IPPROTO_TCP, TCP_NODELAY, (char *)&value, sizeof(int)) < 0)
        __die("TCP_NODELAY failed");

    /* Give the server a chance */
    usleep(1000);

    /* Send this */
    if (send(_sock, (char*)&_c_msg, sizeof(_c_msg), 0) != sizeof(_c_msg))
        __die("send() failed to send message");

    /* Now read the echo */
    if (recv(_sock, (char*)&_o_msg, sizeof(_o_msg), 0) != sizeof(_o_msg))
        __die("recv() failed to read respond");

    printf("Server:%s",&_o_msg);

    close(_sock);
}


int main(int argc, char *argv[])
{
    unsigned short _port;             /* Echo server port */
    char * _host;                     /* Server IP address (dotted quad) */
    char * _c_msg;
    mfram _fram;


    if (argc != 3)                    /* Test for correct number of arguments */
    {
        fprintf(stderr, "Usage: %s \
            <Server IP> \
            <Server Port> \
            <Action Number> 0 login|1 publish|2 subscribe\
            \n", argv[0]);
        exit(1);
    }

    switch(atoi(arg[3]) 
    {
        case 0: _fram = login(); break;
        case 1: _fram = publish(); break;
        case 2: _fram = subscribe(); break;
    }

    _c_msg = (char*)&_fram;           /* message to server */
    printf("Message To be Sended: %s\n",_fram._content_);
    handle(_host,_port,_c_msg);

    exit(0);
}
