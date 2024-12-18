import React, { useCallback, useEffect, useRef, useState } from "react";
import type { MouseEventHandler } from "react";
import Portal, { createContainer } from "../portal/portal";
import styles from "./modalWindow.module.css";

interface ModalProps {
  title: string;
  onClose?: () => void;
  children: React.ReactNode;
}

const MODAL_CONTAINER_ID = "modal-container-id";

const ModalWindow = ({ title, onClose, children }: ModalProps) => {
  const [isMounted, setMounted] = useState(false);
  const rootRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    createContainer({ id: MODAL_CONTAINER_ID });
    setMounted(true);
  }, []);

  useEffect(() => {
    const handleWrapperClick = (event: MouseEvent) => {
      const { target } = event;
      if (target instanceof Node && rootRef.current === target) {
        onClose?.();
      }
    };
    const handleEscapePress = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        onClose?.();
      }
    };

    window.addEventListener("click", handleWrapperClick);
    window.addEventListener("keydown", handleEscapePress);

    return () => {
      window.removeEventListener("click", handleWrapperClick);
      window.removeEventListener("keydown", handleEscapePress);
    };
  }, [onClose]);

  const handleClose: MouseEventHandler<HTMLDivElement | HTMLButtonElement> =
    useCallback(() => {
      onClose?.();
    }, []);

  return isMounted ? (
    <Portal id={MODAL_CONTAINER_ID}>
      <div ref={rootRef} className={styles.wrap}>
        <div className={styles.content}>
          <button
            type="button"
            onClick={handleClose}
            className={styles.closeButton}
          >
            X
          </button>
          <div className="modal-body">{children}</div>
        </div>
      </div>
    </Portal>
  ) : null;
};

export default ModalWindow;
