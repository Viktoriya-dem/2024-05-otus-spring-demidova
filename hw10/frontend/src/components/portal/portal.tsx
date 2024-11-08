import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

interface PortalProps {
  id?: string;
  children: React.ReactNode;
}

type containerOptions = {
  id: string;
  mountNode?: HTMLElement;
};

const createContainer = (options: containerOptions) => {
  if (document.getElementById(options.id)) {
    return;
  }

  const { id, mountNode = document.body } = options;

  const portalContainer = document.createElement("div");
  portalContainer.setAttribute("id", id);
  portalContainer.setAttribute("data-testid", `portalContainer-${id}`);
  mountNode.appendChild(portalContainer);
};
const Portal = ({ id, children }: PortalProps) => {
  const [container, setContainer] = useState<HTMLElement>();

  useEffect(() => {
    if (id) {
      const portalContainer: any = document.getElementById(id);
      setContainer(portalContainer);
    }
  }, [id]);

  return container ? createPortal(children, container) : null;
};

export { createContainer };
export default Portal;
