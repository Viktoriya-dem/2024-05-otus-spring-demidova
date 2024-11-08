export function validator(data: string | any, validatorConfig: any) {
  const errors: any = {};
  function validate(validateMethod: any, data: string, validatorConfig: any) {
    let statusValidate;
    switch (validateMethod) {
      case "isRequired": {
        if (typeof data === "boolean") {
          statusValidate = !data;
        } else {
          statusValidate = "";
        }
        break;
      }

      default:
        break;
    }
    if (statusValidate) return validatorConfig.message;
  }
  for (const fieldName in data) {
    for (const validateMethod in validatorConfig[fieldName]) {
      const error = validate(
        validateMethod,
        data[fieldName],
        validatorConfig[fieldName][validateMethod]
      );
      if (error && !errors[fieldName]) {
        errors[fieldName] = error;
      }
    }
  }
  return errors;
}
