// 定义初始化参数
const initialParams = {
    logo: {
        opacity: 1,
        top: "30%",
        left: "50%",
        transform: "translate(-50%, -50%) scale(0.07)",
        zIndex: 1, // 添加 z-index 属性
    },
    logo1to7: [{
            top: "50%",
            left: "10%",
            transform: "translate(-50%, -50%) scale(0.04)",
            opacity: "0",
            zIndex: 0, // 可根据需要设置 logo1 - 7 的 z-index
        },
        {
            top: "80%",
            left: "20%",
            transform: "translate(-50%, -50%) scale(0.03)",
            opacity: "0",
            zIndex: 0,
        },
        {
            top: "75%",
            left: "40%",
            transform: "translate(-50%, -50%) scale(0.05)",
            opacity: "0",
            zIndex: 0,
        },
        {
            top: "70%",
            left: "70%",
            transform: "translate(-50%, -50%) scale(0.02)",
            opacity: "0",
            zIndex: 0,
        },
        {
            top: "60%",
            left: "80%",
            transform: "translate(-50%, -50%) scale(0.06)",
            opacity: "0",
            zIndex: 0,
        },
        {
            top: "45%",
            left: "90%",
            transform: "translate(-50%, -50%) scale(0.04)",
            opacity: "0",
            zIndex: 0,
        },
        {
            top: "90%",
            left: "90%",
            transform: "translate(-50%, -50%) scale(0.03)",
            opacity: "0",
            zIndex: 0,
        },
    ],
    topCircle: {
        top: "-10%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        zIndex: 0,
    },
    bottomCircle: {
        top: "110%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        zIndex: 0,
    },
    loginRegister: {
        top: "100%",
        left: "20%",
        transform: "translate(-50%, -50%)",
        opacity: 0,
        zIndex: 0,
    },
};

export const animation2Positions = {
    initial: initialParams,
    // 0.8 秒内 logo 隐藏，logo1 - 7 移动到特殊位置
    first08s: {
        logo: {
            opacity: 0,
            top: "30%",
            left: "50%",
            transform: "translate(-50%, -50%) scale(0.07)",
            zIndex: 1, // 添加 z-index 属性
        },
        logo1to7: [{
                top: "90%",
                left: "20%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "120%",
                left: "20%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "120%",
                left: "40%",
                transform: "translate(-50%, -50%) scale(0.05)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "120%",
                left: "60%",
                transform: "translate(-50%, -50%) scale(0.02)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "110%",
                left: "70%",
                transform: "translate(-50%, -50%) scale(0.06)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "90%",
                left: "70%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "140%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "1",
                zIndex: 0,
            },
        ],
        topCircle: {
            top: "110%",
            left: "100%",
            transform: "translate(-50%, -50%)",
            zIndex: 0,
        },
        bottomCircle: {
            top: "110%",
            left: "0%",
            transform: "translate(-50%, -50%)",
            zIndex: 0,
        },
        loginRegister: {
            top: "100%",
            left: "20%",
            transform: "translate(-50%, -50%)",
            opacity: 1,
            zIndex: 0,
        },
    },
    // 保持 0.8 秒后的位置
    second08s: {
        loginRegister: {
            top: "35%",
            left: "20%",
            transform: "translate(-50%, -50%)",
            opacity: 1,
            zIndex: 0,
        },
        logo1to7: [{
                top: "80%",
                left: "30%",
                transform: "translate(-50%, -50%) scale(0.07)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "30%",
                left: "26%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "60%",
                left: "55%",
                transform: "translate(-50%, -50%) scale(0.05)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "90%",
                left: "70%",
                transform: "translate(-50%, -50%) scale(0.02)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "60%",
                left: "85%",
                transform: "translate(-50%, -50%) scale(0.05)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "57%",
                left: "72%",
                transform: "translate(-50%, -50%) scale(0.06)",
                opacity: "1",
                zIndex: 0,
            },
            {
                top: "140%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "1",
                zIndex: 0,
            },
        ],
    },
};