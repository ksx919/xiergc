// scripts/animation1.js
export const animation1Positions = {
    // 初始位置
    initial: {
        logo: {
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%) scale(0.07)",
        },
        topCircle: { top: "10%", left: "50%", transform: "translate(-50%, -50%)" },
        bottomCircle: {
            top: "90%",
            left: "50%",
            transform: "translate(-50%, -50%)",
        },
        logo1to7: [{
                top: "50%",
                left: "10%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "0",
            },
            {
                top: "80%",
                left: "20%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "0",
            },
            {
                top: "75%",
                left: "40%",
                transform: "translate(-50%, -50%) scale(0.05)",
                opacity: "0",
            },
            {
                top: "70%",
                left: "70%",
                transform: "translate(-50%, -50%) scale(0.02)",
                opacity: "0",
            },
            {
                top: "60%",
                left: "80%",
                transform: "translate(-50%, -50%) scale(0.06)",
                opacity: "0",
            },
            {
                top: "45%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "0",
            },
            {
                top: "90%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "0",
            },
        ],
    },
    // 0.8 秒后的位置
    after08s: {
        logo: {
            top: "30%",
            left: "50%",
            transform: "translate(-50%, -50%) scale(0.07)",
        },
        topCircle: { top: "-10%", left: "50%", transform: "translate(-50%, -50%)" },
        bottomCircle: {
            top: "110%",
            left: "50%",
            transform: "translate(-50%, -50%)",
        },
        logo1to7: [{
                top: "50%",
                left: "10%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "1",
            },
            {
                top: "80%",
                left: "20%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "1",
            },
            {
                top: "75%",
                left: "40%",
                transform: "translate(-50%, -50%) scale(0.05)",
                opacity: "1",
            },
            {
                top: "70%",
                left: "70%",
                transform: "translate(-50%, -50%) scale(0.02)",
                opacity: "1",
            },
            {
                top: "60%",
                left: "80%",
                transform: "translate(-50%, -50%) scale(0.06)",
                opacity: "1",
            },
            {
                top: "45%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.04)",
                opacity: "1",
            },
            {
                top: "90%",
                left: "90%",
                transform: "translate(-50%, -50%) scale(0.03)",
                opacity: "1",
            },
        ],
    },
};